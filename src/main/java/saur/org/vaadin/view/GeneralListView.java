package saur.org.vaadin.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import saur.org.vaadin.dto.annotation.Style;
import saur.org.vaadin.dto.annotation.Title;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Tag("universalView")
public class GeneralListView<T> extends Component {

    private Grid<T> grid;
    private final NativeLabel validationMessage = new NativeLabel();
    private final HorizontalLayout toolbar;
    private final Class<T> cls;

    public GeneralListView(Map<String, Supplier<List<T>>> tabsConfig, Class<T> cls, List<T> initialData) {
        this.cls = cls;
        addClassName("list-view");
        toolbar = createToolbar(createTabs(tabsConfig), new Button("Добавить"));
        grid = createGrid(initialData);
        configureGridColumns();
    }

    public Component[] getLayoutComponents() {
        return new Component[]{toolbar, grid, validationMessage};
    }

    private HorizontalLayout createToolbar(Component... toolBarComponents) {
        var toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        if (ObjectUtils.isNotEmpty(toolBarComponents)) {
            Arrays.stream(toolBarComponents).forEach(component -> {
                if (component != null) toolbar.add(component);
            });
//            toolbar.add(toolBarComponents);
        }
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private Tabs createTabs(Map<String, Supplier<List<T>>> tabsConfig) {
        if (tabsConfig == null) return null;
        Tabs tabs = new Tabs();
        tabs.setWidthFull();
        tabsConfig.keySet().forEach(title -> tabs.add(new Tab(title)));
        tabs.addSelectedChangeListener(selectedChangeEvent -> grid.setItems(tabsConfig.get(selectedChangeEvent.getSelectedTab().getLabel()).get()));
        return tabs;
    }

    private Grid<T> createGrid(List<T> data) {
        grid = new Grid<>(cls, false);
        grid.addClassNames("contact-grid");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
        grid.setItems(data);
        String subTitle;
        try {
            subTitle = (String) cls.getMethod("getSUB_TITLE").invoke(null);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        grid.addAttachListener(getAttachListener(subTitle));
        return grid;
    }

    private void configureGridColumns() {
        List<String> columnNames = new ArrayList<>();
        List<Method> columnGetters = new ArrayList<>();
        List<String> columnTitles = new ArrayList<>();
        List<Method> columnSetters = new ArrayList<>();
        List<Set<String>> columnStyles = new ArrayList<>();

        try {
            for (Field field : cls.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    columnNames.add(field.getName());
                    if (field.isAnnotationPresent(Style.class)) {
                        columnTitles.add(field.getAnnotation(Title.class).value());
                    } else columnTitles.add(null);
                    columnGetters.add(cls.getDeclaredMethod("get" + StringUtils.capitalize(field.getName())));
                    columnSetters.add(cls.getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), field.getType()));
                    if (field.isAnnotationPresent(Style.class)) {
                        columnStyles.add(Arrays.stream(field.getAnnotation(Style.class).value()).collect(Collectors.toSet()));
                    } else columnStyles.add(null);
                }
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        Editor<T> editor = grid.getEditor();
        Binder<T> binder = new Binder<>(cls);
        editor.setBinder(binder);
        editor.setBuffered(true);
        editor.addCancelListener(e -> validationMessage.setText(""));

        prepareDataColumns(columnNames, columnGetters, columnTitles, columnSetters, columnStyles, binder);
        prepareControlColumn(editor);

    }

    private void prepareControlColumn(Editor<T> editor) {
        Grid.Column<T> editColumn = grid.addComponentColumn(record -> {
            Button editButton = new Button("Edit", VaadinIcon.EDIT.create());
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(record);
            });
            return editButton;
        });

        Button saveButton = new Button("Save", VaadinIcon.CHECK.create(), e -> {
            if (editor.save()) grid.getDataProvider().refreshAll();
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
    }

    private void prepareDataColumns(List<String> columnNames, List<Method> columnGetters, List<String> columnTitles,
                                    List<Method> columnSetters, List<Set<String>> columnStyles, Binder<T> binder) {
        for (int i = 0; i < columnNames.size(); i++) {
            if (Title.HIDDEN.equals(columnTitles.get(i))) continue;
            Grid.Column<T> gridColumn = grid.addColumn(columnNames.get(i))
                    .setHeader(columnTitles.get(i) != null ? columnTitles.get(i) : columnNames.get(i)).setSortable(true).setAutoWidth(true);

            Optional.ofNullable(columnStyles.get(i)).ifPresent(decoration -> {
                if (ObjectUtils.isNotEmpty(decoration)) {
                    decoration.forEach(gridColumn::addClassName);
                }
            });

            TextField textField = new TextField();
            textField.setWidthFull();

            Method getter = columnGetters.get(i);
            Method setter = columnSetters.get(i);
            binder.forField(textField)
//                    .asRequired("Поле не может быть пустым")
//                    .withStatusLabel(validationMessage)
                    .bind(
                            record -> {
                                try {
                                    Object value = getter.invoke(record);
                                    if (value instanceof Integer) return String.valueOf(value);
                                    else return (String) getter.invoke(record);
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            },
                            (record, value) -> {
                                try {
                                    Class<?> parameterType = setter.getParameterTypes()[0];
                                    if (parameterType == Integer.class) {
                                        Object convertedValue = Integer.valueOf(value);
                                        setter.invoke(record, convertedValue);
                                    } else {
                                        setter.invoke(record, value);
                                    }
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            });
            gridColumn.setEditorComponent(textField);
        }
    }

    private void refreshHeader(Component component, String viewName) {
        do {
            Optional<Component> optionalParent = component.getParent();
            component = optionalParent.orElse(null);
        } while (!(component instanceof MainLayout) && component != null);

        if (component != null) {
            Optional<Component> optionalHeader = component.getChildren().filter(it -> it instanceof HorizontalLayout).findFirst();
            optionalHeader.ifPresent(header -> {
                Optional<Component> optionalLogo = header.getChildren().filter(it -> it instanceof H1).findFirst();
                optionalLogo.ifPresent(h1 -> {
                    H1 logo = (H1) h1;
                    logo.removeFromParent();
                    H1 refreshedLogo = new H1(logo.getText().replaceAll(": .*", "") + ": " + viewName);
                    refreshedLogo.addClassNames(
                            LumoUtility.FontSize.LARGE,
                            LumoUtility.Margin.MEDIUM);

                    ((HorizontalLayout) header).add(refreshedLogo);
                });

            });
        }
    }

    protected ComponentEventListener<AttachEvent> getAttachListener(String viewName) {
        return attachEvent -> {
            Component component = attachEvent.getSource();
            refreshHeader(component, viewName);
        };
    }
}

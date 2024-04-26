package saur.org.vaadin.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import saur.org.vaadin.dto.annotation.Style;
import saur.org.vaadin.dto.annotation.Title;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Tag("universalView")
public class GeneralListView<T> extends Component {

    private final HorizontalLayout toolbar;
    private Grid<T> grid;
    private final NativeLabel validationMessage;
    private final Class<T> cls;
    private final ColumnsConfig columnsConfig;

    @SneakyThrows
    public GeneralListView(Map<String, Supplier<List<T>>> tabsConfig, Class<T> cls,
                           Supplier<List<T>> readMethod, Consumer<T> saveMethod, Consumer<T> deleteMethod) {
        this.cls = cls;
        String subTitle = (String) cls.getMethod("getSUB_TITLE").invoke(null);
        columnsConfig = new ColumnsConfig();
        addClassName("list-view");
        toolbar = createToolbar(createTabs(tabsConfig), createAddButton(subTitle, readMethod, saveMethod));
        grid = createGrid(readMethod, saveMethod, deleteMethod, subTitle);
        validationMessage = new NativeLabel();
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
        }
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private Grid<T> createGrid(Supplier<List<T>> readMethod, Consumer<T> saveMethod, Consumer<T> deleteMethod, String subTitle) {
        grid = new Grid<>(cls, false);
        grid.addClassNames("contact-grid");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
        grid.addAttachListener(getAttachListener(subTitle, readMethod));
        Editor<T> editor = grid.getEditor();
        Binder<T> binder = new Binder<>(cls);
        editor.setBinder(binder);
        editor.setBuffered(true);
        editor.addCancelListener(e -> validationMessage.setText(""));
        prepareDataColumns(binder);
        prepareControlColumn(editor, readMethod, saveMethod, deleteMethod);
        return grid;
    }

    private Button createAddButton(String subTitle, Supplier<List<T>> readMethod, Consumer<T> saveMethod) {
        return new Button("Добавить", e -> {
            Dialog dialog = new Dialog();
            dialog.setHeaderTitle(subTitle);

            VerticalLayout dialogLayout = createDialogLayout();
            dialog.add(dialogLayout);

            Button saveButton = createSaveButton(dialog, readMethod, saveMethod);
            Button cancelButton = new Button("Cancel", cancelEvent -> dialog.close());
            dialog.getFooter().add(cancelButton);
            dialog.getFooter().add(saveButton);
            dialog.open();
        });
    }

    private VerticalLayout createDialogLayout() {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        for (int i = 0; i < columnsConfig.columnNames.size(); i++) {
            if (Title.HIDDEN.equals(columnsConfig.columnTitles.get(i))) continue;
            TextField dialogField = new TextField(columnsConfig.columnTitles.get(i));
            dialogLayout.add(dialogField);
        }

        return dialogLayout;
    }

    private Button createSaveButton(Dialog dialog, Supplier<List<T>> readMethod, Consumer<T> saveMethod) {
        Button saveButton = new Button("Add", clickEvent -> {
            List<TextField> textFields = dialog.getChildren().findFirst().map(Component::getChildren)
                    .orElse(Stream.empty()).map(field -> (TextField) field).toList();
            T item;
            try {
                item = cls.getDeclaredConstructor(columnsConfig.parametersTypes).newInstance(columnsConfig.parameters);
                int hiddenFields = 0;
                for (int i = 0; i < columnsConfig.fieldsCount; i++) {
                    if (Title.HIDDEN.equals(columnsConfig.columnTitles.get(i))) {
                        hiddenFields++;
                        continue;
                    }
                    if (columnsConfig.parametersTypes[i] == Integer.class) {
                        columnsConfig.columnSetters.get(i).invoke(item, Integer.valueOf(textFields.get(i - hiddenFields).getValue()));
                    } else {
                        columnsConfig.columnSetters.get(i).invoke(item, textFields.get(i - hiddenFields).getValue());
                    }

                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException ex) {
                throw new RuntimeException(ex);
            }
            saveMethod.accept(item);
            dialog.close();
            grid.setItems(readMethod.get());
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        return saveButton;
    }

    private Tabs createTabs(Map<String, Supplier<List<T>>> tabsConfig) {
        if (tabsConfig == null) return null;
        Tabs tabs = new Tabs();
        tabs.setWidthFull();
        tabsConfig.keySet().forEach(title -> tabs.add(new Tab(title)));
        tabs.addSelectedChangeListener(selectedChangeEvent -> grid.setItems(tabsConfig.get(selectedChangeEvent.getSelectedTab().getLabel()).get()));
        return tabs;
    }

    private class ColumnsConfig {
        private final List<String> columnNames = new ArrayList<>();
        private final List<Method> columnGetters = new ArrayList<>();
        private final List<String> columnTitles = new ArrayList<>();
        private final List<Method> columnSetters = new ArrayList<>();
        private final List<Set<String>> columnStyles = new ArrayList<>();
        private final Object[] parameters;
        private final Class<?>[] parametersTypes;
        private final int fieldsCount;

        @SneakyThrows
        private ColumnsConfig() {
            List<Field> fields = Arrays.stream(cls.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).toList();
            parameters = new Object[fields.size()];
            parametersTypes = new Class[fields.size()];
            fieldsCount = fields.size();

            for (int i = 0; i < fieldsCount; i++) {
                Field field = fields.get(i);
                columnNames.add(field.getName());
                if (field.isAnnotationPresent(Title.class)) {
                    columnTitles.add(field.getAnnotation(Title.class).value());
                } else columnTitles.add(null);
                columnGetters.add(cls.getDeclaredMethod("get" + StringUtils.capitalize(field.getName())));
                columnSetters.add(cls.getDeclaredMethod("set" + StringUtils.capitalize(field.getName()), field.getType()));
                if (field.isAnnotationPresent(Style.class)) {
                    columnStyles.add(Arrays.stream(field.getAnnotation(Style.class).value()).collect(Collectors.toSet()));
                } else columnStyles.add(null);
                Class<?> type = fields.get(i).getType();
                parametersTypes[i] = type;
                if (type.equals(Integer.class)) {
                    parameters[i] = null;
                } else {
                    parameters[i] = type.getDeclaredConstructor().newInstance();
                }
            }
        }
    }

    private void prepareControlColumn(Editor<T> editor, Supplier<List<T>> readMethod, Consumer<T> saveMethod, Consumer<T> deleteMethod) {
        Grid.Column<T> editColumn = grid.addComponentColumn(record -> {
            Button editButton = new Button("Изменить", VaadinIcon.EDIT.create());
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                grid.getEditor().editItem(record);
            });

            HorizontalLayout buttons = new HorizontalLayout(editButton);
            if (deleteMethod != null) {
                Button deleteButton = new Button("Удалить", VaadinIcon.DEL.create());
                deleteButton.addClickListener(e -> {
                    try {
                        deleteMethod.accept(record);
                        grid.setItems(readMethod.get());
                    } catch (ConstraintViolationException | DataIntegrityViolationException exception) {
                        Dialog messageBox = new Dialog();
                        messageBox.add(new VerticalLayout(new NativeLabel("Нельзя удалить запись! Есть связанные данные."), new Button("Закрыть", event -> messageBox.close())));
                        messageBox.setModal(false);
                        messageBox.setDraggable(true);
                        messageBox.open();
                    }
                });
                buttons.add(deleteButton);
            }
            return buttons;
        });

        Button saveButton = new Button("Сохранить", VaadinIcon.CHECK.create(), e -> {
            T item = editor.getItem();
            if (editor.save()) {
                grid.getDataProvider().refreshAll();
                if (saveMethod != null) saveMethod.accept(item);
            }
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

    private void prepareDataColumns(Binder<T> binder) {
        for (int i = 0; i < columnsConfig.columnNames.size(); i++) {
            if (Title.HIDDEN.equals(columnsConfig.columnTitles.get(i))) continue;
            Grid.Column<T> gridColumn = grid.addColumn(columnsConfig.columnNames.get(i))
                    .setHeader(columnsConfig.columnTitles.get(i) != null ? columnsConfig.columnTitles.get(i) :
                            columnsConfig.columnNames.get(i)).setSortable(true).setAutoWidth(true);

            Optional.ofNullable(columnsConfig.columnStyles.get(i)).ifPresent(decoration -> {
                if (ObjectUtils.isNotEmpty(decoration)) {
                    decoration.forEach(gridColumn::addClassName);
                }
            });

            TextField textField = new TextField();
            textField.setWidthFull();

            Method getter = columnsConfig.columnGetters.get(i);
            Method setter = columnsConfig.columnSetters.get(i);
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

    private ComponentEventListener<AttachEvent> getAttachListener(String viewName, Supplier<List<T>> readMethod) {
        return attachEvent -> {
            Component component = attachEvent.getSource();
            refreshHeader(component, viewName);
            grid.setItems(readMethod.get());
        };
    }
}

package saur.org.vaadin.view;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import saur.org.vaadin.enums.ColumnDecorationType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GeneralListView<T> extends VerticalLayout {

    private Class<T> cls;
    protected Grid<T> grid;

    public GeneralListView(Class<T> cls) {
        this.cls = cls;
        addClassName("list-view");
        setSizeFull();
        createGrid();
        configureGridColumns();
    }

    private void createGrid() {
        grid = new Grid<>(cls, false);
        grid.addClassNames("contact-grid");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setSizeFull();
    }
    private void configureGridColumns() {
        String[] columnHeaders;
        ColumnDecorationType[] columnDecorations;
        List<String> columnNames;
        try {
            columnNames = Arrays.stream(cls.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers())).map(Field::getName).collect(Collectors.toList());
            columnHeaders = (String[]) cls.getMethod("getColumnNames").invoke(null);
            columnDecorations = (ColumnDecorationType[]) cls.getMethod("getColumnDecorations").invoke(null);


            for (int i = 0; i < columnNames.size(); i++) {
                Grid.Column<?> gridColumn = grid.addColumn(columnNames.get(i)).setHeader(columnHeaders[i]).setSortable(true).setAutoWidth(true);
                Optional.of(columnDecorations[i]).ifPresent(decoration -> {
                    if (ObjectUtils.isNotEmpty(decoration.getStyles())) {
                        decoration.getStyles().forEach(gridColumn::addClassName);
                    }
                });
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        // ToDo: inline editing
//        Editor<T> editor = grid.getEditor();
//        Grid.Column<T> editColumn = grid.addComponentColumn(person -> {
//            Button editButton = new Button("Edit");
//            editButton.addClickListener(e -> {
//                if (editor.isOpen())
//                    editor.cancel();
//                grid.getEditor().editItem(person);
//            });
//            return editButton;
//        });
//
//        Binder<T> binder = new Binder<>(cls);
//        editor.setBinder(binder);
//        editor.setBuffered(true);
    }

    private HorizontalLayout getToolbar(Component[] toolBarComponents) {
        var toolbar = new HorizontalLayout();
        toolbar.setWidthFull();
        if (ObjectUtils.isNotEmpty(toolBarComponents)) {
            toolbar.add(toolBarComponents);
        }
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    protected void applyLayout(Component... toolBarComponents) {
        add(getToolbar(toolBarComponents), grid);
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
        return new ComponentEventListener<AttachEvent>() {
            @Override
            public void onComponentEvent(AttachEvent attachEvent) {
                Component component = attachEvent.getSource();
                refreshHeader(component, viewName);
            }
        };
    }

}

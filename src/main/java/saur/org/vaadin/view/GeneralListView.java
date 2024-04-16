package saur.org.vaadin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import saur.org.vaadin.enums.ColumnDecorationType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GeneralListView<T> extends VerticalLayout {
    private Grid<T> grid; // = new Grid<>();
    private final TextField filterText = new TextField();

    public GeneralListView(List<T> data, Class<T> cls) { //String[] columnNames) {
        grid = new Grid<>(cls, false);
        addClassName("list-view");
        setSizeFull();
//        List<Map.Entry<String, String>> columns = new ArrayList<>();
        List<Triple<String, String, ColumnDecorationType>> columns = new ArrayList<>();
        ;
        String[] columnHeaders;
        ColumnDecorationType[] columnDecorations;
        List<String> columnNames;
        try {
            columnNames = Arrays.stream(cls.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers())).map(Field::getName).collect(Collectors.toList());
            columnHeaders = (String[]) cls.getMethod("getColumnNames").invoke(null);
            columnDecorations = (ColumnDecorationType[]) cls.getMethod("getColumnDecorations").invoke(null);
            for (int i = 0; i < columnNames.size(); i++) {
//                columns.add(new AbstractMap.SimpleEntry<>(columnNames.get(i), columnHeaders[i]));
                columns.add(Triple.of(columnNames.get(i), columnHeaders[i], columnDecorations[i]));
//                grid.addColumn(columnNames.get(i)).setHeader(columnHeaders[i]);
//                grid.addComponentColumn()
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        configureGrid(data, columns);
        grid.setItems(data);
        add(getToolbar(), grid);
    }

    //    private void configureGrid(List<T> data, List<Map.Entry<String, String>> columns) {
    private void configureGrid(List<T> data, List<Triple<String, String, ColumnDecorationType>> columns) {

        grid.addClassNames("contact-grid");
        grid.setSizeFull();
//        grid.setColumns("firstName", "lastName");
        for (Triple<String, String, ColumnDecorationType> column : columns) {
            Grid.Column<?> gridColumn = grid.addColumn(column.getLeft()).setHeader(column.getMiddle()).setSortable(true).setAutoWidth(true);
            if (column.getRight() == ColumnDecorationType.WRAPPED) {
                gridColumn.addClassName(LumoUtility.FlexWrap.WRAP);
            }
        }

//        columns.forEach(column -> grid.addColumn(column.getLeft()).setHeader(column.getMiddle()).setSortable(true).setAutoWidth(true));
        grid.setItems(data);
//        grid.setColumns(columnNames);
//        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);

        Button addContactButton = new Button("Add contact");

        var toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

}

package saur.org.vaadin.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import saur.org.vaadin.entity.Author;
import saur.org.vaadin.entity.Entity;
import saur.org.vaadin.repository.AuthorRepository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GeneralListView<T extends Entity> extends VerticalLayout {
    private Grid<T> grid; // = new Grid<>();
    private final TextField filterText = new TextField();

    public GeneralListView(JpaRepository<T, Long> repository, Class<T> cls) { //String[] columnNames) {
        grid = new Grid<>(cls, false);
        addClassName("list-view");
        setSizeFull();
        List<Map.Entry<String, String>> columns = new ArrayList<>();
        String[] columnHeaders;
        List<String> columnNames;
        try {
            columnNames = Arrays.stream(cls.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).map(Field::getName).collect(Collectors.toList());
            columnHeaders = (String[])cls.getMethod("getColumnNames").invoke(null);
            for (int i = 0; i < columnNames.size() - 1; i++) {
                columns.add(new AbstractMap.SimpleEntry<>(columnNames.get(i + 1), columnHeaders[i]));
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        configureGrid(repository.findAll(), columns);
        add(getToolbar(), grid);
    }

    private void configureGrid(List<T> data, List<Map.Entry<String, String>> columns) {

        grid.addClassNames("contact-grid");
        grid.setSizeFull();
//        grid.setColumns("firstName", "lastName");
        columns.forEach(column -> grid.addColumn(column.getKey()).setHeader(column.getValue()).setSortable(true).setAutoWidth(true));
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

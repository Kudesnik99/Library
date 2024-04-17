package saur.org.vaadin.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Triple;
import saur.org.vaadin.enums.ColumnDecorationType;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public class GeneralListView<T> extends VerticalLayout {

    private Class<T> cls;
    protected Grid<T> grid;

    public GeneralListView(Class<T> cls) {
        this.cls = cls;
        grid = new Grid<>(cls, false);
        addClassName("list-view");
        setSizeFull();
    }

    protected void configureGrid() {
        List<Triple<String, String, ColumnDecorationType>> columns = new ArrayList<>();
        String[] columnHeaders;
        ColumnDecorationType[] columnDecorations;
        List<String> columnNames;
        try {
            columnNames = Arrays.stream(cls.getDeclaredFields())
                    .filter(field -> !Modifier.isStatic(field.getModifiers())).map(Field::getName).collect(Collectors.toList());
            columnHeaders = (String[]) cls.getMethod("getColumnNames").invoke(null);
            columnDecorations = (ColumnDecorationType[]) cls.getMethod("getColumnDecorations").invoke(null);
            for (int i = 0; i < columnNames.size(); i++) {
                columns.add(Triple.of(columnNames.get(i), columnHeaders[i], columnDecorations[i]));
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        for (Triple<String, String, ColumnDecorationType> column : columns) {
            Grid.Column<?> gridColumn = grid.addColumn(column.getLeft()).setHeader(column.getMiddle()).setSortable(true).setAutoWidth(true);
            if (column.getRight() == ColumnDecorationType.WRAPPED) {
                gridColumn.addClassName(LumoUtility.FlexWrap.WRAP);
            }
        }
    }

    private HorizontalLayout getToolbar(Component[] toolBarComponents) {
        var toolbar = new HorizontalLayout();
        if (ObjectUtils.isNotEmpty(toolBarComponents)) {
            toolbar.add(toolBarComponents);
        }
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    protected void applyLayout(Component... toolBarComponents) { //Component[] toolBarComponents) {
        add(getToolbar(toolBarComponents), grid);
    }

}

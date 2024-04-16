package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import saur.org.vaadin.enums.ColumnDecorationType;

@Data
@AllArgsConstructor
public class ReaderMainView {
    @Getter
    private static String[] columnNames = new String[]{"Id", "Имя", "Фамилия"};

    @Getter
    private static ColumnDecorationType[] columnDecorations = new ColumnDecorationType[]{null, null};

    private String firstName;

    private String lastName;
}

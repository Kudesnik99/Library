package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import saur.org.vaadin.enums.ColumnDecorationType;

@Data
@AllArgsConstructor
public class ReaderDto {
    @Getter
    private static String[] columnNames = new String[]{"Имя", "Фамилия", "Книги на руках"};

    @Getter
    private static ColumnDecorationType[] columnDecorations = new ColumnDecorationType[]{null, null, ColumnDecorationType.WRAPPED};

    private String firstName;

    private String lastName;

    private String books;
}

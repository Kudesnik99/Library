package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import saur.org.vaadin.enums.ColumnDecorationType;

import static saur.org.vaadin.enums.ColumnDecorationType.ORDINARY;
import static saur.org.vaadin.enums.ColumnDecorationType.WRAPPED;

@Data
@AllArgsConstructor
public class BookDto {
    @Getter
    private static String[] columnNames = new String[]{"Название", "Год издания", "Издательство", "Автор(ы)", "На руках у", "ISBN"};

    @Getter
    private static ColumnDecorationType[] columnDecorations = new ColumnDecorationType[]{ORDINARY, ORDINARY, ORDINARY, WRAPPED, ORDINARY, ORDINARY};

    private String title;

    private Integer publicationDate;

    private String publisher;

    private String authors;

    private String reader;

    private String ISBN;
}

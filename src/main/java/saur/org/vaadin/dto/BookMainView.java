package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import saur.org.vaadin.enums.ColumnDecorationType;

@Data
@AllArgsConstructor
public class BookMainView {
    @Getter
    private static String[] columnNames = new String[]{"Название", "Год издания", "Издательство", "Автор(ы)", "На руках у", "ISBN"};

    @Getter
    private static ColumnDecorationType[] columnDecorations = new ColumnDecorationType[]{null, null, null, ColumnDecorationType.WRAPPED, null, null};

    private String title;

    private Integer publicationDate;

    private String publisher;

    private String authors;

    private String reader;

    private String ISBN;
}

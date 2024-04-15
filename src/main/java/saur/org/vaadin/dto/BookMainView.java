package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class BookMainView {
    @Getter
    private static String[] columnNames = new String[]{"Название", "Год издания", "Издательство", "Автор", "На руках у", "ISBN"};

    private String title;

    private Integer publicationDate;

    private String publisher;

    private String author;

    private String reader;

    private String ISBN;
}
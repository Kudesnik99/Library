package saur.org.vaadin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Book extends saur.org.vaadin.entity.Entity {
    @Getter
    private static String ENTITY_NAME = "Книги";
    @Getter
    private static String[] columnNames = new String[]{"Название", "Год издания", "Издательство", "Автор", "На руках у", "ISBN"};

    public Book(String title, Integer publicationDate, String publisher, Integer authorId, Integer readerId, String ISBN) {
        this.title = title;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        this.authorId = authorId;
        this.readerId = readerId;
        this.ISBN = ISBN;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String title;

    private Integer publicationDate;

    private String publisher;

    private Integer authorId;

    private Integer readerId;

    private String ISBN;
}

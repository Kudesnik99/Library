package saur.org.vaadin.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
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

    @Id
    @GeneratedValue
    private Integer bookId;

    private String title;

    private Integer publicationDate;

    private String publisher;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    private Integer readerId;

    private String ISBN;
}

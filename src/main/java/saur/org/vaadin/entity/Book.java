package saur.org.vaadin.entity;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @ManyToMany
    @JoinTable(name="author_book", joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @ManyToOne
    @JoinColumn(name = "reader_id")
    private Reader reader;

    private String ISBN;
}

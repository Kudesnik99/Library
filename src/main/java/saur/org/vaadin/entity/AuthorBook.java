package saur.org.vaadin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;

@Entity
@IdClass(AuthorBookId.class)
public class AuthorBook {
    @Id
    @Column(name = "author_id")
    private Integer authorId;

    @Id
    @Column(name = "book_id")
    private Integer bookId;
}

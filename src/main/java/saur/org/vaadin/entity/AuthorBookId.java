package saur.org.vaadin.entity;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class AuthorBookId implements Serializable {
    private Integer authorId;
    private Integer bookId;
}

package saur.org.vaadin.view;

import saur.org.vaadin.entity.Author;
import saur.org.vaadin.entity.Book;
import saur.org.vaadin.entity.Reader;

public enum Binder {
    AUTHOR(Author.class, Author.class, "Авторы"),
    BOOK(Book.class, BookListView.class, "Книги"),
    READER(Reader.class, ReaderListView.class, "Читатели");

    private final Class<?> entityClass;
    private final Class<?> viewClass;
    private final String title;

    Binder(Class<?> entityClass, Class<?> viewClass, String title) {
        this.entityClass = entityClass;
        this.viewClass = viewClass;
        this.title = title;
    }
}

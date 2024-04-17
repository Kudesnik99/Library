package saur.org.vaadin.dto.mapper;

import org.springframework.util.ObjectUtils;
import saur.org.vaadin.dto.BookDto;
import saur.org.vaadin.entity.Book;

public class BookMapper {
    public static BookDto entityToMainView (Book bookEntity) {
        return new BookDto(
                bookEntity.getTitle(),
                bookEntity.getPublicationDate(),
                bookEntity.getPublisher(),
                ObjectUtils.isEmpty(bookEntity.getAuthors()) ? "" :
                        bookEntity.getAuthors().stream().map(author -> author.getFirstName() + " " + author.getLastName())
                                .reduce("", (subtotal, element) -> ObjectUtils.isEmpty(subtotal) ? element : subtotal + "; " + element),
                ObjectUtils.isEmpty(bookEntity.getReader()) ? "" : bookEntity.getReader().getFirstName() + " " + bookEntity.getReader().getLastName(),
                bookEntity.getISBN()
                );
    }
}

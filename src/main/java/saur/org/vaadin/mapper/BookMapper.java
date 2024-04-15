package saur.org.vaadin.mapper;

import org.springframework.util.ObjectUtils;
import saur.org.vaadin.dto.BookMainView;
import saur.org.vaadin.entity.Book;

public class BookMapper {
    public static BookMainView entityToMainView (Book bookEntity) {
        return new BookMainView(
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

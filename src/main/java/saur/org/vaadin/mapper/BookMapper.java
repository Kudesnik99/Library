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
                bookEntity.getAuthor().getFirstName() + " " + bookEntity.getAuthor().getLastName(),
                ObjectUtils.isEmpty(bookEntity.getReaderId()) ? "" : String.valueOf(bookEntity.getReaderId()),
                bookEntity.getISBN()
                );
    }
}

package saur.org.vaadin.dto.mapper;

import org.springframework.util.ObjectUtils;
import saur.org.vaadin.dto.ReaderDto;
import saur.org.vaadin.entity.Book;
import saur.org.vaadin.entity.Reader;

public class ReaderMapper {
    public static ReaderDto entityToMainView (Reader readerEntity) {
        return new ReaderDto(
                readerEntity.getFirstName(),
                readerEntity.getLastName(),
                ObjectUtils.isEmpty(readerEntity.getBooks()) ? "" :
                        readerEntity.getBooks().stream().map(Book::getTitle)
                                .reduce("", (subtotal, element) -> ObjectUtils.isEmpty(subtotal) ? element : subtotal + "; " + element)
        );
    }
}

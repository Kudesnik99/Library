package saur.org.vaadin.dto.mapper;

import lombok.AllArgsConstructor;
import org.springframework.util.ObjectUtils;
import saur.org.vaadin.dto.ReaderDto;
import saur.org.vaadin.entity.Book;
import saur.org.vaadin.entity.Reader;
import saur.org.vaadin.repository.BookRepository;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class ReaderMapper {

    public static ReaderDto entityToMainView (Reader readerEntity) {
        return new ReaderDto(
                readerEntity.getReaderId(),
                readerEntity.getFirstName(),
                readerEntity.getLastName(),
                ObjectUtils.isEmpty(readerEntity.getBooks()) ? "" :
                        readerEntity.getBooks().stream().map(Book::getTitle)
                                .reduce("", (subtotal, element) -> ObjectUtils.isEmpty(subtotal) ? element : subtotal + "; " + element)
        );
    }

    public static Reader mainViewToEntity(ReaderDto readerDto, BookRepository bookRepository) {
        List<Book> books = bookRepository.findBooksByTitleIn(Arrays.stream(readerDto.getBooks().split("; ")).toList());
        return new Reader(
                readerDto.getReaderId(),
                readerDto.getFirstName(),
                readerDto.getLastName(),
                books);
    }
}

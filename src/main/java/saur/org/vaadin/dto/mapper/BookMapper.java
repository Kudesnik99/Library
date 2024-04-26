package saur.org.vaadin.dto.mapper;

import org.springframework.util.ObjectUtils;
import saur.org.vaadin.dto.BookDto;
import saur.org.vaadin.entity.Author;
import saur.org.vaadin.entity.Book;
import saur.org.vaadin.entity.Reader;
import saur.org.vaadin.repository.AuthorRepository;
import saur.org.vaadin.repository.ReaderRepository;

import java.util.Arrays;
import java.util.Set;

public class BookMapper {
    public static BookDto entityToMainView(Book bookEntity) {
        return new BookDto(
                bookEntity.getBookId(),
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

    public static Book mainViewToEntity(BookDto bookDto, AuthorRepository authorRepository, ReaderRepository readerRepository) {
        Set<Author> authors = authorRepository.findAuthorByLastNameInAndFirstNameIn(
                Arrays.stream(bookDto.getAuthors().split("; ")).map(author -> author.split(" ")[1]).toList(),
                Arrays.stream(bookDto.getAuthors().split("; ")).map(author -> author.split(" ")[0]).toList());
        Reader reader = null;
        if (!ObjectUtils.isEmpty(bookDto.getReader())) {
            reader = readerRepository.findReaderByLastNameInAndFirstNameIn(
                    Arrays.stream(bookDto.getReader().split("; ")).map(author -> author.split(" ")[1]).toList(),
                    Arrays.stream(bookDto.getReader().split("; ")).map(author -> author.split(" ")[0]).toList());
        }
        return new Book(
                bookDto.getBookId(),
                bookDto.getTitle(),
                bookDto.getPublicationDate(),
                bookDto.getPublisher(),
                authors,
                reader,
                bookDto.getISBN());
    }
}

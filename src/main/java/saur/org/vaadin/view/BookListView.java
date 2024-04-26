package saur.org.vaadin.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import saur.org.vaadin.dto.BookDto;
import saur.org.vaadin.dto.mapper.BookMapper;
import saur.org.vaadin.dto.mapper.ReaderMapper;
import saur.org.vaadin.repository.AuthorRepository;
import saur.org.vaadin.repository.BookRepository;
import saur.org.vaadin.repository.ReaderRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Книги")
public class BookListView extends VerticalLayout {

    @Getter
    private static final String VIEW_NAME = "Книги";
    private static final String MORE_TWO_AUTHORS = "Больше 2-х авторов";
    private static final String BORROWED_BOOKS = "Книги на руках";
    private static final String ALL = "Все";

    private final BookRepository bookRepository;


    public BookListView(BookRepository bookRepository, AuthorRepository authorRepository, ReaderRepository readerRepository) {
        this.bookRepository = bookRepository;

        Map<String, Supplier<List<BookDto>>> tabsConfig = new LinkedHashMap<>();
        tabsConfig.put(ALL, () -> this.bookRepository.findAllJoinAuthors().stream().map(BookMapper::entityToMainView).toList());
        tabsConfig.put(MORE_TWO_AUTHORS, () -> bookRepository.findMoreThenNAuthors(2).stream().map(BookMapper::entityToMainView).toList());
        tabsConfig.put(BORROWED_BOOKS, () -> bookRepository.findBorrowedBooks().stream().map(BookMapper::entityToMainView).toList());

        GeneralListView<BookDto> generalListView = new GeneralListView<>(tabsConfig, BookDto.class, tabsConfig.get(ALL),
                (record) -> bookRepository.save(BookMapper.mainViewToEntity(record, authorRepository, readerRepository)), null);
        setSizeFull();
        add(generalListView.getLayoutComponents());
    }
}

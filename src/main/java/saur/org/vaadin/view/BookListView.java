package saur.org.vaadin.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.dto.BookMainView;
import saur.org.vaadin.entity.Book;
import saur.org.vaadin.mapper.BookMapper;
import saur.org.vaadin.repository.BookRepository;

@Route(value="Book", layout = MainLayout.class)
@PageTitle("Книги")
public class BookListView extends GeneralListView<BookMainView> {
    public BookListView(BookRepository bookRepository) {
       super(bookRepository.findAll().stream().map(BookMapper::entityToMainView).toList(), BookMainView.class);
    }
}

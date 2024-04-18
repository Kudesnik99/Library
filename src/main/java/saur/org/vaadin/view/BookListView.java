package saur.org.vaadin.view;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import saur.org.vaadin.dto.BookDto;
import saur.org.vaadin.dto.mapper.BookMapper;
import saur.org.vaadin.repository.BookRepository;

@Route(value = "Book", layout = MainLayout.class)
@PageTitle("Книги")
public class BookListView extends GeneralListView<BookDto> {
    @Getter
    private static final String VIEW_NAME = "Книги";
    private static final String MORE_TWO_AUTHORS = "Больше 2-х авторов";
    private static final String BORROWED_BOOKS = "Книги на руках";
    private static final String ALL = "Все";

    private final BookRepository bookRepository;

    public BookListView(BookRepository bookRepository) {
        super(BookDto.class);
        this.bookRepository = bookRepository;
        grid.setItems(this.bookRepository.findAllJoinAuthors().stream().map(BookMapper::entityToMainView).toList());

        var all = new Tab(ALL);
        var moreThreeAuthors = new Tab(MORE_TWO_AUTHORS);
        var borrowedBooks = new Tab(BORROWED_BOOKS);
        Tabs tabs = new Tabs(all, moreThreeAuthors, borrowedBooks);
        tabs.addSelectedChangeListener(getTabsListener());
        tabs.setWidthFull();

        Button addButton = new Button("Добавить");
        addButton.addClickListener(click -> {
            // ToDo: Написать добавление записи
        });

        this.addAttachListener(getAttachListener(VIEW_NAME));
        applyLayout(tabs, addButton);
    }


    private ComponentEventListener<Tabs.SelectedChangeEvent> getTabsListener() {

        return new ComponentEventListener<Tabs.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(Tabs.SelectedChangeEvent selectedChangeEvent) {
                switch (selectedChangeEvent.getSelectedTab().getLabel()) {
                    case ALL ->
                            grid.setItems(bookRepository.findAllJoinAuthors().stream().map(BookMapper::entityToMainView).toList());
                    case MORE_TWO_AUTHORS ->
                            grid.setItems(bookRepository.findMoreThenNAuthors(2).stream().map(BookMapper::entityToMainView).toList());
                    case BORROWED_BOOKS ->
                            grid.setItems(bookRepository.findBorrowedBooks().stream().map(BookMapper::entityToMainView).toList());
                }
            }
        };
    }


}

package saur.org.vaadin.view;

import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.dto.AuthorMainView;
import saur.org.vaadin.mapper.AuthorMapper;
import saur.org.vaadin.mapper.BookMapper;
import saur.org.vaadin.repository.AuthorRepository;

@Route(value="", layout = MainLayout.class)
@PageTitle("Авторы")
public class AuthorListView extends GeneralListView<AuthorMainView> {

    private final String MOST_POPULAR = "Топ 3 читаемых авторов";
    private final String ALL = "Все";

    private final AuthorRepository authorRepository;

    public AuthorListView(AuthorRepository authorRepository) {
        super(AuthorMainView.class);
        this.authorRepository = authorRepository;
        configureGrid();
        grid.setItems(this.authorRepository.findAll().stream().map(AuthorMapper::entityToMainView).toList());

        var all = new Tab(ALL);
        var mostPopular = new Tab(MOST_POPULAR);

        Tabs tabs = new Tabs(all, mostPopular);
        tabs.addSelectedChangeListener(getTabsListener());
        applyLayout(tabs);
    }

    private ComponentEventListener<Tabs.SelectedChangeEvent> getTabsListener () {
        return new ComponentEventListener<Tabs.SelectedChangeEvent>() {
            @Override
            public void onComponentEvent(Tabs.SelectedChangeEvent selectedChangeEvent) {
                switch (selectedChangeEvent.getSelectedTab().getLabel()) {
                    case ALL -> grid.setItems(authorRepository.findAll().stream().map(AuthorMapper::entityToMainView).toList());
                    case MOST_POPULAR -> grid.setItems(authorRepository.findPopularAuthors(3).stream().map(AuthorMapper::entityToMainView).toList());
                }
                System.out.println(">>> " + selectedChangeEvent.getSelectedTab().getLabel());
            }
        };
    }
}

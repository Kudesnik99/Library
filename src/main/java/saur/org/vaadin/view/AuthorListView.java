package saur.org.vaadin.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.dto.AuthorMainView;
import saur.org.vaadin.mapper.AuthorMapper;
import saur.org.vaadin.repository.AuthorRepository;

@Route(value="", layout = MainLayout.class)
@PageTitle("Авторы")
public class AuthorListView extends GeneralListView<AuthorMainView> {

    private final AuthorRepository authorRepository;

    public AuthorListView(AuthorRepository authorRepository) {
        super(AuthorMainView.class);
        this.authorRepository = authorRepository;
        configureGrid();
        grid.setItems(this.authorRepository.findAll().stream().map(AuthorMapper::entityToMainView).toList());
        applyLayout();
    }
}

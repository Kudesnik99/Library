package saur.org.vaadin.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.dto.AuthorMainView;
import saur.org.vaadin.entity.Author;
import saur.org.vaadin.mapper.AuthorMapper;
import saur.org.vaadin.repository.AuthorRepository;

@Route(value="", layout = MainLayout.class)
@PageTitle("Авторы")
public class AuthorListView extends GeneralListView<AuthorMainView> {
    public AuthorListView(AuthorRepository authorRepository) {
        super(authorRepository.findAll().stream().map(AuthorMapper::entityToMainView).toList(), AuthorMainView.class);
    }
}

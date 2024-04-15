package saur.org.vaadin.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.entity.Author;
import saur.org.vaadin.repository.AuthorRepository;

@Route(value="", layout = MainLayout.class)
@PageTitle("Авторы")
public class AuthorListView extends GeneralListView<Author> {
    public AuthorListView(AuthorRepository authorRepository) {
        super(authorRepository, Author.class);
    }
}

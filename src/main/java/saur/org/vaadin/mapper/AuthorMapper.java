package saur.org.vaadin.mapper;

import saur.org.vaadin.dto.AuthorMainView;
import saur.org.vaadin.entity.Author;

public class AuthorMapper {
    public static AuthorMainView entityToMainView (Author authorEntity) {
        return new AuthorMainView(
                authorEntity.getFirstName(),
                authorEntity.getLastName()
        );
    }
}

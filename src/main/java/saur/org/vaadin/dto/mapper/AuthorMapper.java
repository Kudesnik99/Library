package saur.org.vaadin.dto.mapper;

import saur.org.vaadin.dto.AuthorDto;
import saur.org.vaadin.entity.Author;

public class AuthorMapper {
    public static AuthorDto entityToMainView (Author authorEntity) {
        return new AuthorDto(
                authorEntity.getAuthorId(),
                authorEntity.getFirstName(),
                authorEntity.getLastName()
        );
    }

    public static Author mainViewToEntity (AuthorDto authorDto) {
        return new Author(authorDto.getAuthorId(),
                authorDto.getFirstName(),
                authorDto.getLastName());
    }
}

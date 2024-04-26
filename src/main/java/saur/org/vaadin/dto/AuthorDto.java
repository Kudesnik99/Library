package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import saur.org.vaadin.dto.annotation.Title;

@Data
@AllArgsConstructor
public class AuthorDto {
    @Getter
    private static final String SUB_TITLE = "Авторы";

    @Title("HIDDEN")
    private Integer authorId;

    @Title("Имя")
    private String firstName;

    @Title("Фамилия")
    private String lastName;
}

package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import saur.org.vaadin.dto.annotation.Title;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderDto {
    @Getter
    private static final String SUB_TITLE = "Читатели";

    @Title("HIDDEN")
    private Integer readerId;

    @Title("Имя")
    private String firstName;

    @Title("Фамилия")
    private String lastName;

    @Title("Книги на руках")
    private String books;
}

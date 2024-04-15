package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class AuthorMainView {
    @Getter
    private static String[] columnNames = new String[]{"Имя", "Фамилия"};

    private String firstName;

    private String lastName;
}
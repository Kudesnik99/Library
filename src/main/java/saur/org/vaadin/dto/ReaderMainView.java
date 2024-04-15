package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class ReaderMainView {
    @Getter
    private static String[] columnNames = new String[]{"Id", "Имя", "Фамилия"};

    private String firstName;

    private String lastName;
}

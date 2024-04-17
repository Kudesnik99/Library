package saur.org.vaadin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import saur.org.vaadin.enums.ColumnDecorationType;

@Data
@AllArgsConstructor
public class AuthorDto {
    @Getter
    private static String[] columnNames = new String[]{"Имя", "Фамилия"};

    @Getter
    private static ColumnDecorationType[] columnDecorations = new ColumnDecorationType[]{null, null};

    private String firstName;

    private String lastName;
}

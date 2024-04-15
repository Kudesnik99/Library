package saur.org.vaadin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Author extends saur.org.vaadin.entity.Entity {
    @Getter
    private static String ENTITY_NAME = "Авторы";
    @Getter
    private static String[] columnNames = new String[]{"Имя", "Фамилия"};

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Id
    @GeneratedValue
    private Integer id;

    private String firstName;

    private String lastName;
}

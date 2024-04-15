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
public class Reader extends saur.org.vaadin.entity.Entity {
    @Getter
    private static String ENTITY_NAME = "Читатели";

    @Id
    @GeneratedValue
    private Integer readerId;

    private String firstName;

    private String lastName;
}

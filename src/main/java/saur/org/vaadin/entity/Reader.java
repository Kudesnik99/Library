package saur.org.vaadin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Reader {
    @Id
    @GeneratedValue
    private Integer readerId;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "reader")
    private List<Book> books;
}

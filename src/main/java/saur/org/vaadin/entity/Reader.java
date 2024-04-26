package saur.org.vaadin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Reader {
    @Id
    @GeneratedValue(generator = "seq_reader")
    @SequenceGenerator(name = "seq_reader", allocationSize = 1)
    private Integer readerId;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "reader")
    private List<Book> books;
}

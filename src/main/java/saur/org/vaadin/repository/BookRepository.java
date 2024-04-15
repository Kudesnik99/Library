package saur.org.vaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saur.org.vaadin.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}

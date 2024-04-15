package saur.org.vaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saur.org.vaadin.entity.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}

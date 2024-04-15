package saur.org.vaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import saur.org.vaadin.entity.Reader;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
}

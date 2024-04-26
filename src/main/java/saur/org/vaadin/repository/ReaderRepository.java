package saur.org.vaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import saur.org.vaadin.entity.Reader;

import java.util.List;
import java.util.Set;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
    @Query("select r from Reader r left join fetch r.books")
    List<Reader> findAllJoinBooks();

    @Query("select r from Reader r inner join fetch r.books")
    List<Reader> findAllWithBooks();

    Reader findReaderByLastNameInAndFirstNameIn(List<String> lastNames, List<String> firstNames);
}

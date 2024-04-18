package saur.org.vaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import saur.org.vaadin.entity.Book;
import saur.org.vaadin.entity.Reader;

import java.util.List;

@Repository
public interface ReaderRepository extends JpaRepository<Reader, Long> {
    @Query("select r from Reader r join fetch r.books")
    List<Reader> findAllJoinBooks();
//    @Query("select r from Reader r where r.books is not null")
//    List<Reader> findAllWithBooks();
}

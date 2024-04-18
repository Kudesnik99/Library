package saur.org.vaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import saur.org.vaadin.entity.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {


//    @Query("select a from Author a where a.authorId in (select b from Book b join fetch b.authors)");
    // @Query("select b from Book b join fetch b.authors where b.bookId in (select ab.bookId from AuthorBook ab group by ab.bookId having count(ab.bookId) >= ?1)")
    //@Query("select a from Author a where a.authorId in (select a1.authorId, count(a1.authorId) from Author a1 join fetch a1.authorId join fetch )")
    @Query(value = "select r.author_id, r.first_name, r.last_name from " +
            "(select a.first_name, a.last_name, a.author_id, count(*) from author a " +
            "join author_book ab on a.author_id=ab.author_id join book b on b.book_id=ab.book_id " +
            "where b.reader_id is not null " +
            "group by a.first_name, a.last_name, a.author_id) r " +
            "order by r.count desc limit ?1",
    nativeQuery = true)
    List<Author> findPopularAuthors(int N);

    // select r.first_name, r.last_name, r.count from (select a.first_name, a.last_name, a.author_id, count(*)
    // from author a join author_book ab on a.author_id=ab.author_id
    // join book b on b.book_id=ab.book_id where b.reader_id is not null group by a.first_name, a.last_name, a.author_id) r
    // order by r.count desc limit 3;
}

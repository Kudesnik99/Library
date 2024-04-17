package saur.org.vaadin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import saur.org.vaadin.entity.Book;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("select b from Book b join fetch b.authors")
    List<Book> findAllJoinAuthors();


    @Query("select b from Book b join fetch b.authors where b.bookId in (select ab.bookId from AuthorBook ab group by ab.bookId having count(ab.bookId) >= ?1)")
    List<Book> findMoreThenNAuthors(int N);

    @Query("select b from Book b join fetch b.authors where b.reader is not null")
    List<Book> findBorrowedBooks();



    //    @Query("select b.bookId from Book b join fetch b.authors group by b.bookId having count(b.bookId) >= ?1")
//    @Query("select b from Book b where b.bookId in (select ba.bookId from Book b join fetch b.authors group by b.bookId having count(b.bookId) >= 2)")

//    @Query( value = "SELECT b1_0.book_id, b1_0.isbn, a1_0.book_id, a1_1.author_id, a1_1.first_name, a1_1.last_name, " +
//            "b1_0.publication_date, b1_0.publisher, b1_0.reader_id, b1_0.title FROM book b1_0 " +
//            "join author_book a1_0 on b1_0.book_id=a1_0.book_id " +
//            "join author a1_1 on a1_1.author_id=a1_0.author_id " +
//            "where b1_0.book_id in (select ab.book_id from author_book ab group by ab.book_id having count(ab.book_id) >= 2)",
//        nativeQuery = true)

    // select book_id from author_book group by book_id having count(book_id) >= 2;

    // select * from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id
    // select r.title from (select * from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id) r where r.first_name = 'Конан';
    // select r.title from (select * from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id) r where count(r.first_name) >= 3;
    // Авторов, которые написали больше или равно 2-х книг:
    // select r.first_name, r.last_name, count(*) from (select * from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id) r group by r.first_name, r.last_name having count(r.first_name) >=2;
    //
    // Названия книг, у которых больше или равно 2-х авторов
    // select r.title, count(*) from (select * from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id) r group by r.title having count(r.title) >=2;
    // select r.book_id, r.title from (select b.book_id, b.title from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id) r group by r.book_id, r.title having count(r.book_id) >=2;
    // select b.book_id, b.title from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id group by b.book_id, b.title having count(b.book_id) >= 2;
    // select * from book b where b.book_id in (select b.book_id from book b join author_book a1 on b.book_id=a1.book_id join author a2 on a2.author_id=a1.author_id group by b.book_id having count(b.book_id) >= 2);

    //select
    //        b1_0.book_id,
    //        b1_0.isbn,
    //        a1_0.book_id,
    //        a1_1.author_id,
    //        a1_1.first_name,
    //        a1_1.last_name,
    //        b1_0.publication_date,
    //        b1_0.publisher,
    //        b1_0.reader_id,
    //        b1_0.title
    //    from
    //        book b1_0
    //    join
    //        author_book a1_0
    //            on b1_0.book_id=a1_0.book_id
    //    join
    //        author a1_1
    //            on a1_1.author_id=a1_0.author_id
}

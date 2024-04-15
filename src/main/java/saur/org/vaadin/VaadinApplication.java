package saur.org.vaadin;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import saur.org.vaadin.entity.Author;
import saur.org.vaadin.entity.Book;
import saur.org.vaadin.entity.Reader;
import saur.org.vaadin.repository.AuthorRepository;
import saur.org.vaadin.repository.BookRepository;
import saur.org.vaadin.repository.ReaderRepository;

@SpringBootApplication
public class VaadinApplication {
	public static void main(String[] args) {
		SpringApplication.run(VaadinApplication.class, args);
	}
//	@Bean
//	public CommandLineRunner loadData(AuthorRepository authorRepository,
//									  ReaderRepository readerRepository,
//									  BookRepository bookRepository) {
//		return (args) -> {
//			authorRepository.save(new Author("Маклин", "Алистер"));
//			authorRepository.save(new Author("Александр", "Пушкин"));
//			authorRepository.save(new Author("Конан", "Дойл"));
//
//			readerRepository.save(new Reader("Алексей", "Ларистов"));
//			readerRepository.save(new Reader("Андрей", "Ларистов"));
//			readerRepository.save(new Reader("Дмитрий", "Ларистов"));
//			readerRepository.save(new Reader("Ольга", "Ларистова"));
//
//			bookRepository.save(new Book("Том Сойер", 1975, "ДетГиз", 2, null, "1234214"));
//		};
//	}
}

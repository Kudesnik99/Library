package saur.org.vaadin.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import saur.org.vaadin.dto.BookDto;
import saur.org.vaadin.dto.ReaderDto;
import saur.org.vaadin.dto.mapper.BookMapper;
import saur.org.vaadin.dto.mapper.ReaderMapper;
import saur.org.vaadin.repository.BookRepository;
import saur.org.vaadin.repository.ReaderRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Route(value = "Reader", layout = MainLayout.class)
@PageTitle("Читатели")
public class ReaderListView extends VerticalLayout {
    @Getter
    private static final String VIEW_NAME = "Читатели";
    private static final String ALL = "Все";
    private static final String WITH_BOOKS = "С книгами на руках";
    public ReaderListView(ReaderRepository readerRepository, BookRepository bookRepository) {
        Map<String, Supplier<List<ReaderDto>>> tabsConfig = new LinkedHashMap<>();
        tabsConfig.put(ALL, () -> readerRepository.findAllJoinBooks().stream().map(ReaderMapper::entityToMainView).toList());
        tabsConfig.put(WITH_BOOKS, () -> readerRepository.findAllWithBooks().stream().map(ReaderMapper::entityToMainView).toList());

        GeneralListView<ReaderDto> generalListView = new GeneralListView<>(tabsConfig, ReaderDto.class,
                () -> readerRepository.findAllJoinBooks().stream().map(ReaderMapper::entityToMainView).toList(),
                (record) -> readerRepository.save(ReaderMapper.mainViewToEntity(record, bookRepository)), null);
        setSizeFull();
        add(generalListView.getLayoutComponents());
    }
}

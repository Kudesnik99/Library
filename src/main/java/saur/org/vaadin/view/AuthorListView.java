package saur.org.vaadin.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import saur.org.vaadin.dto.AuthorDto;
import saur.org.vaadin.dto.mapper.AuthorMapper;
import saur.org.vaadin.repository.AuthorRepository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Route(value = "Author", layout = MainLayout.class)
@PageTitle("Авторы")
public class AuthorListView extends VerticalLayout {
    @Getter
    private static final String VIEW_NAME = "Авторы";

    private static final String ALL = "Все";
    private static final String MOST_POPULAR = "Топ 3 читаемых авторов";


    private final AuthorRepository authorRepository;

    public AuthorListView(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;

        Map<String, Supplier<List<AuthorDto>>> tabsConfig = new LinkedHashMap<>();
        tabsConfig.put(ALL, () -> this.authorRepository.findAll().stream().map(AuthorMapper::entityToMainView).toList());
        tabsConfig.put(MOST_POPULAR, () -> authorRepository.findPopularAuthors(2).stream().map(AuthorMapper::entityToMainView).toList());

        GeneralListView<AuthorDto> generalListView = new GeneralListView<>(tabsConfig, AuthorDto.class, tabsConfig.get(ALL).get());
        setSizeFull();
        add(generalListView.getLayoutComponents());
    }

}

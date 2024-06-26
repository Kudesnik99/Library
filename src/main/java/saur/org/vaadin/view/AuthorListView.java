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
    private static final String MOST_POPULAR = "Топ 2 читаемых авторов";
    public AuthorListView(AuthorRepository authorRepository) {
        Map<String, Supplier<List<AuthorDto>>> tabsConfig = new LinkedHashMap<>();
        tabsConfig.put(ALL, () -> authorRepository.findAll().stream().map(AuthorMapper::entityToMainView).toList());
        tabsConfig.put(MOST_POPULAR, () -> authorRepository.findPopularAuthors(2).stream().map(AuthorMapper::entityToMainView).toList());

        GeneralListView<AuthorDto> generalListView = new GeneralListView<>(tabsConfig, AuthorDto.class, tabsConfig.get(ALL),
                record -> authorRepository.save(AuthorMapper.mainViewToEntity(record)),
                record -> authorRepository.delete(AuthorMapper.mainViewToEntity(record)));
        setSizeFull();
        add(generalListView.getLayoutComponents());
    }

}

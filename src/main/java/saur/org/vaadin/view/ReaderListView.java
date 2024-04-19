package saur.org.vaadin.view;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import saur.org.vaadin.dto.ReaderDto;
import saur.org.vaadin.dto.mapper.ReaderMapper;
import saur.org.vaadin.repository.ReaderRepository;

@Route(value = "Reader", layout = MainLayout.class)
@PageTitle("Читатели")
public class ReaderListView extends VerticalLayout {
    @Getter
    private static final String VIEW_NAME = "Читатели";

    private final ReaderRepository readerRepository;

    public ReaderListView(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;

        GeneralListView<ReaderDto> generalListView = new GeneralListView<>(null, ReaderDto.class, this.readerRepository
                .findAllJoinBooks().stream().map(ReaderMapper::entityToMainView).toList());
        setSizeFull();
        add(generalListView.getLayoutComponents());
    }
}

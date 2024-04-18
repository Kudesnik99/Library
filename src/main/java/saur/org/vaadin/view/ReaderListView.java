package saur.org.vaadin.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.Getter;
import saur.org.vaadin.dto.ReaderDto;
import saur.org.vaadin.dto.mapper.ReaderMapper;
import saur.org.vaadin.repository.ReaderRepository;

@Route(value="Reader", layout = MainLayout.class)
@PageTitle("Читатели")
public class ReaderListView extends GeneralListView<ReaderDto> {
    @Getter
    private static final String VIEW_NAME = "Читатели";

    private final ReaderRepository readerRepository;

    public ReaderListView(ReaderRepository readerRepository) {
        super(ReaderDto.class);
        this.readerRepository = readerRepository;
        grid.setItems(this.readerRepository.findAllJoinBooks().stream().map(ReaderMapper::entityToMainView).toList());
        this.addAttachListener(getAttachListener(VIEW_NAME));
        applyLayout();
    }
}

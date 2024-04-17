package saur.org.vaadin.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.dto.ReaderMainView;
import saur.org.vaadin.mapper.ReaderMapper;
import saur.org.vaadin.repository.ReaderRepository;

@Route(value="Reader", layout = MainLayout.class)
@PageTitle("Читатели")
public class ReaderListView extends GeneralListView<ReaderMainView> {

    private final ReaderRepository readerRepository;

    public ReaderListView(ReaderRepository readerRepository) {
        super(ReaderMainView.class);
        this.readerRepository = readerRepository;
        configureGrid();
        grid.setItems(this.readerRepository.findAll().stream().map(ReaderMapper::entityToMainView).toList());
        applyLayout();
    }
}

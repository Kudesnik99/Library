package saur.org.vaadin.view;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import saur.org.vaadin.entity.Reader;
import saur.org.vaadin.repository.ReaderRepository;

@Route(value="Reader", layout = MainLayout.class)
@PageTitle("Читатели")
public class ReaderListView extends GeneralListView<Reader> {
    public ReaderListView(ReaderRepository readerRepository) {
        super(readerRepository.findAll(), Reader.class);
    }
}

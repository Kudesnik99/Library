package saur.org.vaadin.mapper;

import saur.org.vaadin.dto.ReaderMainView;
import saur.org.vaadin.entity.Reader;

public class ReaderMapper {
    public static ReaderMainView entityToMainView (Reader readerEntity) {
        return new ReaderMainView(
                readerEntity.getFirstName(),
                readerEntity.getLastName()
        );
    }
}

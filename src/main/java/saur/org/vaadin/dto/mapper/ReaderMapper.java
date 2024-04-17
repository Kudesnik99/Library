package saur.org.vaadin.dto.mapper;

import saur.org.vaadin.dto.ReaderDto;
import saur.org.vaadin.entity.Reader;

public class ReaderMapper {
    public static ReaderDto entityToMainView (Reader readerEntity) {
        return new ReaderDto(
                readerEntity.getFirstName(),
                readerEntity.getLastName()
        );
    }
}

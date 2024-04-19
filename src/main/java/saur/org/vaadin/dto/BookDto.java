package saur.org.vaadin.dto;

import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import saur.org.vaadin.dto.annotation.Style;
import saur.org.vaadin.dto.annotation.Title;

@Data
@AllArgsConstructor
public class BookDto {
    @Getter
    private static final String SUB_TITLE = "Книги";

    @Title("Название")
    private String title;

    @Title("Год издания")
    private Integer publicationDate;

    @Title("Издательство")
    private String publisher;

    @Style(LumoUtility.FlexWrap.WRAP)
    @Title("Автор(ы)")
    private String authors;

    @Title("Наруках у")
    private String reader;

    @Title("ISBN")
    private String ISBN;
}

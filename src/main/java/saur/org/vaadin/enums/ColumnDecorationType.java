package saur.org.vaadin.enums;

import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.Getter;

import java.util.Set;

public enum ColumnDecorationType {
    ORDINARY(null),
    WRAPPED(Set.of(LumoUtility.FlexWrap.WRAP));

    @Getter
    private final Set<String> styles;

    ColumnDecorationType(Set<String> styles) {
        this.styles = styles;
    }
}

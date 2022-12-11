package sqb.uz.task.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static sqb.uz.task.helper.AppMessages.EMPTY_FIELD;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Filter {
    @NotNull(message = EMPTY_FIELD)
    private String fieldName;
//    @NotNull(message = EMPTY_FIELD)
    private String value;
    @NotNull(message = EMPTY_FIELD)
    private String typeValue;
    private Boolean isBetween;
    private Between between;
}

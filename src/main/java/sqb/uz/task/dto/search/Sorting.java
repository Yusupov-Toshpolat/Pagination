package sqb.uz.task.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static sqb.uz.task.helper.AppMessages.EMPTY_FIELD;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sorting {
    @NotNull(message = EMPTY_FIELD)
    private String fieldName;
    private String sortType;
}

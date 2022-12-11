package sqb.uz.task.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import static sqb.uz.task.helper.AppMessages.EMPTY_FIELD;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Between {
    @NotNull(message = EMPTY_FIELD)
    private String low;
    @NotNull(message = EMPTY_FIELD)
    private String high;
}

package sqb.uz.task.dto.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import javax.validation.constraints.NotNull;

import static sqb.uz.task.helper.AppMessages.EMPTY_FIELD;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    @NotNull(message = EMPTY_FIELD)
    private String tableName;
    private Integer page;
    private Integer size;
    private Sorting sorting;
    private List<Filter> filters;
}

package sqb.uz.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    @Min(value = 0, message = "A negative value")
    private Double price;
    private LocalDate dateCreated;
    private Boolean status;
    @Min(value = 0, message = "A negative value")
    private Integer amount;
}

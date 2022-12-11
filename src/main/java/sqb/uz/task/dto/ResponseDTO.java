package sqb.uz.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO <T>{
    private Integer code;
    private Boolean success;
    private String message;
    private T data;
}

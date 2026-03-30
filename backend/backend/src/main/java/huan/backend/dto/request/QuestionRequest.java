package huan.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    @NotBlank(message = "NOT_NULL")
    private String question;

    @NotEmpty(message = "NOT_NULL")
    @Size(min = 2, message = "INVALID_SIZE")
    private List<String> options;

    @NotNull(message = "NOT_NULL")
    private Integer answer; 

}
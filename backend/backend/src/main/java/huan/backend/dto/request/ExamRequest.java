package huan.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ExamRequest {
    @NotBlank(message = "NOT_NULL")
    private String name;
    
    private String posterUrl; 

    @NotNull(message = "NOT_NULL")
    private LocalDate startTime;

    @NotNull(message = "NOT_NULL")
    private Integer duration;

    @NotNull(message = "NOT_NULL")
    private Long categoryId;

    @Valid 
    @NotEmpty(message = "NOT_NULL")
    private List<QuestionRequest> questions;
}
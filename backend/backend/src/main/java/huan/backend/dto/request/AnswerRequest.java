package huan.backend.dto.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequest {
    private Long questionId;
    private Integer selectedOption;
}
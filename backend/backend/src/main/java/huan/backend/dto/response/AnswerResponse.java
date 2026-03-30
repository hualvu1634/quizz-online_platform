package huan.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerResponse {
    private boolean isCorrect;      
    private Integer correctAnswer;  
}
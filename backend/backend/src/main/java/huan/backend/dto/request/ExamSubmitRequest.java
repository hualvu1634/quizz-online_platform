package huan.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ExamSubmitRequest {
    private Long userId;
    private Long examId;
    private List<AnswerRequest> answers; 
}
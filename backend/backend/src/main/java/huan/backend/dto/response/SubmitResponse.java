package huan.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SubmitResponse {
    private Long userId;
    private Long examId;
    private Double score;
    private int correctAnswers;
    private LocalDateTime submittedAt;
}
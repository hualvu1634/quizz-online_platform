package huan.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ExamSubmitResponse {
    private int score;
    private int totalQuestions;
    private int correctAnswers;
    private boolean isPassed;
    private LocalDateTime submittedAt;
}
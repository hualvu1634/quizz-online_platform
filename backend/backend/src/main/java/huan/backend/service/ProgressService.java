package huan.backend.service;

import huan.backend.dto.request.SubmitRequest;
import huan.backend.dto.response.ResultResponse;
import huan.backend.entity.Exam;
import huan.backend.entity.Progress;
import huan.backend.entity.Question;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.repository.ExamRepository;
import huan.backend.repository.ProgressRepository;
import huan.backend.repository.QuestionRepository;
import huan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ExamRepository examRepository; 
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public ResultResponse submitExam(SubmitRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
                
        List<Question> examQuestions = questionRepository.findByExamId(request.getExamId());
        int totalQuestions = examQuestions.size();
        
        Map<Long, Integer> userAnswers = request.getAnswers();

        int correctAnswers = 0;

        for (Question question : examQuestions) {
            Integer selectedOption = userAnswers.get(question.getId());

            if (selectedOption == null) {
                throw new AppException(ErrorCode.INCOMPLETE_EXAM);
            }

            if (selectedOption.equals(question.getAnswer())) {
                correctAnswers++;
            }
        }

        double score = (double) correctAnswers / totalQuestions * 100.0;
        score = Math.round(score * 100.0) / 100.0; 
        
        LocalDateTime now = LocalDateTime.now();
        Progress progress = Progress.builder()
                .exam(exam)
                .user(user)
                .score(score)
                .submittedAt(now)
                .build();

        progressRepository.save(progress);

        // 5. Trả về kết quả
        return ResultResponse.builder()
                .score(score)
                .correctAnswers(correctAnswers)
                .submittedAt(now)
                .build();
    }
}
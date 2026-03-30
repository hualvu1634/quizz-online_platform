package huan.backend.service;

import huan.backend.dto.request.AnswerRequest;
import huan.backend.dto.request.ExamSubmitRequest;
import huan.backend.dto.response.ExamSubmitResponse;
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


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ExamRepository examRepository; 
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;



    public ExamSubmitResponse submitExam(ExamSubmitRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        List<Question> examQuestions = questionRepository.findByExamId(request.getExamId());
        
        if (examQuestions.isEmpty()) {
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND); // Hoặc tạo ErrorCode EXAM_HAS_NO_QUESTIONS
        }

        // 3. Chuyển danh sách câu trả lời của user thành Map<QuestionId, SelectedOption> để dễ truy xuất
        Map<Long, Integer> userAnswersMap = request.getAnswers().stream()
                .collect(Collectors.toMap(AnswerRequest::getQuestionId, AnswerRequest::getSelectedOption));

        // 4. Chấm điểm
        int correctAnswers = 0;
        int totalQuestions = examQuestions.size();

        for (Question question : examQuestions) {
            Integer selectedOption = userAnswersMap.get(question.getId());
            // Nếu user có chọn đáp án và đáp án đó khớp với đáp án đúng của câu hỏi
            if (selectedOption != null && selectedOption.equals(question.getAnswer())) {
                correctAnswers++;
            }
        }

        // Tính điểm theo thang 100 (Có thể tùy chỉnh lại công thức theo dự án của bạn)
        int score = (int) Math.round((double) correctAnswers / totalQuestions * 100);
        
        // Giả sử điều kiện đỗ là từ 50 điểm trở lên
        boolean isPassed = score >= 50; 
        LocalDateTime now = LocalDateTime.now();
        Progress progress = progressRepository.findByUserIdAndExamId(user.getId(), exam.getId())
                .orElse(Progress.builder().user(user).exam(exam).build());

        progress.setScore(score);
        progress.setIsPassed(isPassed);
        progress.setSubmittedAt(now);

        progressRepository.save(progress);

        // 6. Trả về kết quả cho user
        return ExamSubmitResponse.builder()
                .score(score)
                .totalQuestions(totalQuestions)
                .correctAnswers(correctAnswers)
                .isPassed(isPassed)
                .submittedAt(now)
                .build();
    }
}
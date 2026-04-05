package huan.backend.service;

import huan.backend.dto.request.SubmitRequest;
import huan.backend.dto.response.PageResponse;
import huan.backend.dto.response.SubmitResponse;
import huan.backend.entity.Exam;
import huan.backend.entity.Progress;
import huan.backend.entity.Question;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.mapper.ProgressMapper;
import huan.backend.repository.ExamRepository;
import huan.backend.repository.ProgressRepository;
import huan.backend.repository.QuestionRepository;
import huan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    // Inject MapStruct Mapper
    private final ProgressMapper progressMapper;

    @Transactional
    public SubmitResponse submitExam(SubmitRequest request) {

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

        double score = (double) correctAnswers / totalQuestions * 10.0;
        score = Math.round(score * 100.0) / 100.0; 
        
        LocalDateTime now = LocalDateTime.now();
        Progress progress = Progress.builder()
                .exam(exam)
                .user(user)
                .score(score)
                .submittedAt(now)
                .build();

        progressRepository.save(progress);

        // Trả về kết quả ngay khi thi xong (có đủ correctAnswers)
        return SubmitResponse.builder()
                .userId(request.getUserId())
                .examId(request.getExamId())
                .score(score)
                .correctAnswers(correctAnswers)
                .submittedAt(now)
                .build();
    }

    public PageResponse<SubmitResponse> getUserHistory(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("submittedAt").descending());
        
        Page<Progress> pageData = progressRepository.findByUserId(userId, pageable);
        
        // Dùng MapStruct mapper ở đây
        List<SubmitResponse> responseList = pageData.getContent().stream()
                .map(progressMapper::toSubmitResponse) 
                .collect(Collectors.toList());

        return PageResponse.<SubmitResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responseList)
                .build();
    }
}
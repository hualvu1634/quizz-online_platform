package huan.backend.service;

import huan.backend.dto.request.ExamRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.ExamDetailResponse;
import huan.backend.entity.Category;
import huan.backend.entity.Exam;
import huan.backend.entity.Question;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.mapper.ExamMapper;
import huan.backend.mapper.QuestionMapper;
import huan.backend.repository.CategoryRepository;
import huan.backend.repository.ExamRepository; // Nhớ đổi tên Repository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;
    private final CategoryRepository categoryRepository;
    private final ExamMapper examMapper;
    private final QuestionMapper questionMapper;

    public ExamDetailResponse getExam(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND)); // Định nghĩa lại ErrorCode
        return examMapper.toResponse(exam);
    }

    public ExamDetailResponse createExam(ExamRequest request) {
        if (examRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.EXAM_EXISTED);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        Exam exam = examMapper.toEntity(request);
        exam.setCategory(category);

        if (request.getQuestions() != null && !request.getQuestions().isEmpty()) {
            List<Question> questions = request.getQuestions().stream()
                    .map(qReq -> {
                        Question q = questionMapper.toEntity(qReq);
                        q.setExam(exam);
                        return q;
                    }).collect(Collectors.toList());
            exam.setQuestions(questions);
        }

        return examMapper.toResponse(examRepository.save(exam));
    }

    @Transactional
    public ExamDetailResponse updateExam(Long id, ExamRequest request) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        if (!exam.getName().equals(request.getName()) &&
                examRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.EXAM_EXISTED);
        }

        examMapper.updateEntity(request, exam);

        if (request.getCategoryId() != null && !exam.getCategory().getId().equals(request.getCategoryId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            exam.setCategory(category);
        }
        if (request.getQuestions() != null) {
            exam.getQuestions().clear();
            List<Question> newQuestions = request.getQuestions().stream()
                    .map(qReq -> {
                        Question q = questionMapper.toEntity(qReq);
                        q.setExam(exam);
                        return q;
                    }).collect(Collectors.toList());
            exam.getQuestions().addAll(newQuestions);
        }

        return examMapper.toResponse(examRepository.save(exam));
    }

    public ApiResponse deleteExam(Long id) {
        if (!examRepository.existsById(id)) {
            throw new AppException(ErrorCode.EXAM_NOT_FOUND);
        }
        examRepository.deleteById(id);
        return ApiResponse.builder()
                .message("Xóa bài thi thành công")
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
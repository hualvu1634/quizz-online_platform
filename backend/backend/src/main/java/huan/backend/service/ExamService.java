package huan.backend.service;

import huan.backend.dto.request.ExamRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.ExamDetailResponse;
import huan.backend.dto.response.ExamResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.entity.Category;
import huan.backend.entity.Exam;
import huan.backend.entity.Question;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.mapper.ExamMapper;
import huan.backend.mapper.QuestionMapper;
import huan.backend.repository.CategoryRepository;
import huan.backend.repository.ExamRepository; 
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND)); 
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

        List<Question> questions = request.getQuestions().stream()
                    .map(dto -> {
                        Question q = questionMapper.toEntity(dto);
                        q.setExam(exam);
                        return q;
                    }).collect(Collectors.toList());
        exam.setQuestions(questions);


        return examMapper.toResponse(examRepository.save(exam));
    }

    public PageResponse<ExamResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Exam> pageData = examRepository.findAll(pageable);
        
        List<ExamResponse> responseList = pageData.getContent().stream()
                .map(examMapper::toExamResponse) 
                .collect(Collectors.toList());

        return PageResponse.<ExamResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(responseList)
                .build();
    }

    public ApiResponse deleteExam(Long id) {
        Exam exam = examRepository.findById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        examRepository.delete(exam);
        return ApiResponse.builder()
                .message("Xóa bài thi thành công")
                .code(200)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
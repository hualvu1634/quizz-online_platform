package huan.backend.service;

import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.ExamResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.entity.Category;
import huan.backend.entity.Exam;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.mapper.ExamMapper;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ExamRepository examRepository;
    private final ExamMapper examMapper;

    public Category addCategory(Category request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.CATEGORY_EXISTED);
        }
        Category category = Category.builder().name(request.getName()).build();
        return categoryRepository.save(category);
    }

    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public PageResponse<ExamResponse> getExamsByCategory(Long categoryId, int page, int size) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<Exam> pageData = examRepository.findByCategoryIdAndIsActive(categoryId, true, pageable);

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

    public ApiResponse delCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        categoryRepository.deleteById(id);

        return ApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .code(200)
                .message("Xóa danh mục thành công")
                .build();
    }
}
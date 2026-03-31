package huan.backend.controller;

import huan.backend.dto.response.ApiResponse;

import huan.backend.dto.response.ExamResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.entity.Category;
import huan.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "http://localhost:3000") 
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
   
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Category> addCategory(@RequestBody Category request) {
        return ResponseEntity.ok(categoryService.addCategory(request));
    }
    
    @GetMapping
    public ResponseEntity<List<Category>> getAll() {
        return ResponseEntity.ok(categoryService.getAll());
    }
    @GetMapping("/{id}/exams")
    public ResponseEntity<PageResponse<ExamResponse>> getExamsByCategory(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 10;
        return ResponseEntity.ok(categoryService.getExamsByCategory(id, page, pageSize));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> delCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delCategory(id));
    }
}
package huan.backend.controller;

import huan.backend.dto.request.ExamRequest;
import huan.backend.dto.request.SubmitRequest;
import huan.backend.dto.response.ApiResponse;

import huan.backend.dto.response.ExamDetailResponse;
import huan.backend.dto.response.ExamResponse;
import huan.backend.dto.response.SubmitResponse;
import huan.backend.dto.response.PageResponse;
import huan.backend.service.ExamService; 
import huan.backend.service.ProgressService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exams")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ExamController {
    
    private final ExamService examService;

    private final ProgressService progressService;

    @GetMapping("/{id}")
    public ResponseEntity<ExamDetailResponse> getExam(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getExam(id));
    }
    @GetMapping
    public ResponseEntity<PageResponse<ExamResponse>> getExam( 
            @RequestParam(value = "page", defaultValue = "1") int page){
        return ResponseEntity.ok(examService.getAll(page,12));
    }
    @PostMapping
    public ResponseEntity<ExamDetailResponse> createExam(@Valid @RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExam(@PathVariable Long id) {
        return ResponseEntity.ok(examService.deleteExam(id));
    }


    @PostMapping("/submit")
    public ResponseEntity<SubmitResponse> submit(@Valid @RequestBody SubmitRequest examSubmitRequest){
        return ResponseEntity.ok(progressService.submitExam(examSubmitRequest));
    }

}
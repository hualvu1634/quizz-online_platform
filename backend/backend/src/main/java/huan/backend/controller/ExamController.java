package huan.backend.controller;

import huan.backend.dto.request.AnswerRequest;
import huan.backend.dto.request.ExamRequest;
import huan.backend.dto.request.ExamSubmitRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.dto.response.AnswerResponse;
import huan.backend.dto.response.ExamDetailResponse;
import huan.backend.dto.response.ExamSubmitResponse;
import huan.backend.service.ExamService; 
import huan.backend.service.ProgressService;
import huan.backend.service.QuestionService;
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
    private final QuestionService questionService;
    private final ProgressService progressService;

    @GetMapping("/{id}")
    public ResponseEntity<ExamDetailResponse> getExam(@PathVariable Long id) {
        return ResponseEntity.ok(examService.getExam(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<ExamDetailResponse> createExam(@Valid @RequestBody ExamRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(examService.createExam(request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ExamDetailResponse> updateExam(@PathVariable Long id, @Valid @RequestBody ExamRequest request) {
        return ResponseEntity.ok(examService.updateExam(id, request));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteExam(@PathVariable Long id) {
        return ResponseEntity.ok(examService.deleteExam(id));
    }

    @PostMapping("/check-answer")
    public ResponseEntity<AnswerResponse> checkAnswer(@Valid @RequestBody AnswerRequest request) {
        return ResponseEntity.ok(questionService.checkAnswer(request));
    }
    @PostMapping("/submit")
    public ResponseEntity<ExamSubmitResponse> submit(@Valid @RequestBody ExamSubmitRequest examSubmitRequest){
        return ResponseEntity.ok(progressService.submitExam(examSubmitRequest));
    }

}
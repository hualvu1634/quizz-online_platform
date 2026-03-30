package huan.backend.service;

import huan.backend.dto.request.ProgressRequest;
import huan.backend.dto.response.ApiResponse;
import huan.backend.entity.Exam;
import huan.backend.entity.Progress;
import huan.backend.entity.User;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.repository.ExamRepository;
import huan.backend.repository.ProgressRepository;
import huan.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final ProgressRepository progressRepository;
    private final ExamRepository lessonRepository;
    private final UserRepository userRepository;

    public ApiResponse markLessonAsCompleted(ProgressRequest request) {
      
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)); 

        Exam quiz = lessonRepository.findById(request.getExamId())
                .orElseThrow(() -> new AppException(ErrorCode.LESSON_NOT_FOUND));

        Progress progress = Progress.builder()
                .user(user)
                .quiz(quiz)
                .build();
                
        progressRepository.save(progress);

        return ApiResponse.builder()
                .message("Lưu tiến trình học thành công!")
                .code(201)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
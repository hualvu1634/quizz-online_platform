package huan.backend.service;

import huan.backend.dto.request.AnswerRequest;
import huan.backend.dto.response.AnswerResponse;
import huan.backend.dto.response.QuestionResponse;
import huan.backend.entity.Question;
import huan.backend.enumerate.ErrorCode;
import huan.backend.exception.AppException;
import huan.backend.mapper.QuestionMapper;
import huan.backend.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    public List<QuestionResponse> getQuestionsByExam(Long examId) {
        List<Question> questions = questionRepository.findByExamId(examId); 
        return questions.stream()
                .map(questionMapper::toResponse)
                .collect(Collectors.toList());
    }


    public AnswerResponse checkAnswer(AnswerRequest request) {
        Question question = questionRepository.findById(request.getQuestionId())
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        boolean isCorrect = request.getSelectedOption().equals(question.getAnswer());

        return AnswerResponse.builder()
                .isCorrect(isCorrect)
                .correctAnswer(question.getAnswer())
                .build();
    }
}
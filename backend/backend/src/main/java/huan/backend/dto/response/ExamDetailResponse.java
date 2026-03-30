package huan.backend.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExamDetailResponse {
    private Long id;
    private String name; 
    private Integer duration;
  
    
    private List<QuestionResponse> questions;
}
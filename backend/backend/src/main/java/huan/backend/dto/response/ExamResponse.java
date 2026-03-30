package huan.backend.dto.response;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ExamResponse {
    private Long id;
    private String name;
    private LocalDate startTime;
    private Integer duration;
    private String posterUrl;
      private Long categoryId;
}
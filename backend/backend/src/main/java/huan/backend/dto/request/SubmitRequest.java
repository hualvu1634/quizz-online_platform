package huan.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
public class SubmitRequest {
    private Long userId;
    private Long examId;
    private Map<Long, Integer> answers; 
}
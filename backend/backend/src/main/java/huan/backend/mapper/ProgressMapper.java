package huan.backend.mapper;

import huan.backend.dto.response.SubmitResponse;
import huan.backend.entity.Progress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProgressMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "exam.id", target = "examId")
    @Mapping(target = "correctAnswers", ignore = true) // Bỏ qua field này vì entity không lưu
    SubmitResponse toSubmitResponse(Progress progress);
}
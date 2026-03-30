package huan.backend.mapper;

import huan.backend.dto.request.QuestionRequest;
import huan.backend.dto.response.QuestionResponse;
import huan.backend.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exam", ignore = true)
    Question toEntity(QuestionRequest questionRequest);

    QuestionResponse toResponse(Question question);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exam", ignore = true)
    void updateEntityFromRequest(QuestionRequest request, @MappingTarget Question entity);
}
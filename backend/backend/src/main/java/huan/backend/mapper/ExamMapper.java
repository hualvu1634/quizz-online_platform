package huan.backend.mapper;

import huan.backend.dto.request.ExamRequest;
import huan.backend.dto.response.ExamDetailResponse; 
import huan.backend.dto.response.ExamResponse;
import huan.backend.entity.Exam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ExamMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "questions", ignore = true) 
    @Mapping(target = "isActive", ignore = true)
    Exam toEntity(ExamRequest request);

    ExamDetailResponse toResponse(Exam exam);
    @Mapping(target = "categoryId", source = "category.id")
    ExamResponse toExamResponse(Exam exam);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateEntity(ExamRequest request, @MappingTarget Exam exam);
}
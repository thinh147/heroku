package com.gogitek.toeictest.mapper;

import com.gogitek.toeictest.controller.dto.request.CreateQuestionRequest;
import com.gogitek.toeictest.controller.dto.response.AdminQuestionResponse;
import com.gogitek.toeictest.controller.dto.response.QuestionResponse;
import com.gogitek.toeictest.entity.QuestionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    @Mapping(target = "isActive", expression = "java(Boolean.TRUE)")
    QuestionEntity requestToEntity(CreateQuestionRequest request);
    List<QuestionEntity> requestsToEntities(List<CreateQuestionRequest> request);
    QuestionResponse entityToResponse(QuestionEntity entity);
    AdminQuestionResponse entityToAdminResponse(QuestionEntity entity);
    void mapRequestToEntity(@MappingTarget QuestionEntity entity, AdminQuestionResponse response);
}

package com.gogitek.toeictest.mapper;

import com.gogitek.toeictest.controller.dto.response.ExamTypeResponse;
import com.gogitek.toeictest.entity.ExamTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExamMapper {
    ExamTypeResponse entityToResponse(ExamTypeEntity entity);
}

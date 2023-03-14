package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.controller.dto.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.CreateQuestionRequest;
import com.gogitek.toeictest.entity.ExamEntity;
import com.gogitek.toeictest.entity.ExamQuestionEntity;
import com.gogitek.toeictest.mapper.QuestionMapper;
import com.gogitek.toeictest.repository.ExamQuestionRepository;
import com.gogitek.toeictest.repository.ExamRepository;
import com.gogitek.toeictest.repository.ExamTypeRepository;
import com.gogitek.toeictest.repository.QuestionRepository;
import com.gogitek.toeictest.service.AdminService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final ExamTypeRepository examTypeRepository;
    private final ExamQuestionRepository examQuestionRepository;
    private final ExamRepository examRepository;

    @Override
    @Transactional
    public void createQuestion(List<CreateQuestionRequest> request) {
        try {
            var questionEntity = questionMapper.requestsToEntities(request);
            questionRepository.saveAll(questionEntity);
        } catch (Exception e) {
            throw new ToeicRuntimeException(ErrorCode.FAILED);
        }
    }

    @Override
    @Transactional
    public void createExam(ExamRequest request) {
        var typeEntity = examTypeRepository
                .findById(request.getTypeId())
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.ID_NOT_FOUND));
        var entity = ExamEntity
                .builder()
                .examType(typeEntity)
                .duration(request.getDuration())
                .build();
        entity = examRepository.save(entity);
        var questionIds = request.getQuestionIds();
        insertRelationTable(entity, questionIds);
    }

    @Transactional
    @Async
    protected void insertRelationTable(final ExamEntity examEntity, List<Long> questionIds) {
        var questionList = questionRepository.findByIdIn(questionIds);
        var relation = questionList
                .stream()
                .map(item -> ExamQuestionEntity
                        .builder()
                        .examEntity(examEntity)
                        .questionEntity(item)
                        .build())
                .collect(Collectors.toSet());
        examQuestionRepository.saveAll(relation);
    }
}

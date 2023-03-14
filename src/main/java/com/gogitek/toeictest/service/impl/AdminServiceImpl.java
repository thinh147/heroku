package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.CreateQuestionRequest;
import com.gogitek.toeictest.controller.dto.request.QuestionFilter;
import com.gogitek.toeictest.controller.dto.response.AdminQuestionResponse;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
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
        try {
            var questionIds = request.getQuestionIds();
            insertRelationTable(entity, questionIds);
        }catch (Exception e){
            LOGGER.info("Dont have question, message: {}", e.getMessage());
        }
    }

    @Override
    public PaginationPage<AdminQuestionResponse> retrieveListQuestionAdmin(QuestionFilter filter) {
        var pageable = PageRequest.of(filter.getPage(), filter.getSize());
        var questionList = questionRepository.findByPartIn(filter.getParts(), pageable);
        return new PaginationPage<AdminQuestionResponse>()
                .setLimit(filter.getSize())
                .setOffset(filter.getPage())
                .setTotalRecords(questionList.getTotalElements())
                .setRecords(questionList
                        .getContent()
                        .stream()
                        .map(questionMapper::entityToAdminResponse)
                        .toList());
    }

    @Override
    public AdminQuestionResponse modifyQuestion(AdminQuestionResponse response) {
        var entity = questionRepository
                .findById(response.getId())
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.ID_NOT_FOUND));
        questionMapper.mapRequestToEntity(entity, response);
        return questionMapper.entityToAdminResponse(entity);
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

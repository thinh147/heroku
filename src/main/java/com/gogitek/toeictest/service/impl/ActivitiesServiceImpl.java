package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.config.pagination.OffsetPageRequest;
import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.config.utils.GsonUtils;
import com.gogitek.toeictest.controller.dto.request.SubmitRequest;
import com.gogitek.toeictest.controller.dto.response.ExamResponse;
import com.gogitek.toeictest.controller.dto.response.ExamTypeResponse;
import com.gogitek.toeictest.controller.dto.response.QuestionResponse;
import com.gogitek.toeictest.controller.dto.response.VocabGroupResponse;
import com.gogitek.toeictest.entity.*;
import com.gogitek.toeictest.entity.base.IdBase;
import com.gogitek.toeictest.mapper.ExamMapper;
import com.gogitek.toeictest.mapper.QuestionMapper;
import com.gogitek.toeictest.mapper.VocabularyMapper;
import com.gogitek.toeictest.repository.*;
import com.gogitek.toeictest.security.SecurityUtils;
import com.gogitek.toeictest.service.ActivitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivitiesServiceImpl implements ActivitiesService {
    private final GroupRepository groupRepository;
    private final VocabularyMapper vocabularyMapper;
    private final ExamTypeRepository examTypeRepository;
    private final ExamMapper examMapper;
    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final ExamRepository examRepository;
    private final UserAnswerRepository userAnswerRepository;

    @Override
    public PaginationPage<VocabGroupResponse> retrieveGroupPage(Integer offset, Integer limit) {
        var pageable = new OffsetPageRequest(offset, limit);
        var groupEntities = groupRepository.findAll(pageable);
        var res = new PaginationPage<VocabGroupResponse>();
        var dtoList = groupEntities.getContent().stream().map(vocabularyMapper::entityToDto).toList();
        return res.setRecords(dtoList)
                .setOffset(offset)
                .setLimit(limit)
                .setTotalRecords(groupEntities.getTotalElements());
    }

    @Override
    public List<ExamTypeResponse> retrieveExamType() {
        return examTypeRepository
                .findAll()
                .stream()
                .map(examMapper::entityToResponse)
                .toList();
    }

    @Override
    public PaginationPage<QuestionResponse> retrieveQuestionByExamId(Long examId, Integer offset, Integer limit) {
        var pageable = new OffsetPageRequest(offset, limit);
        var entityList = questionRepository.findByExamId(examId, pageable);
        return new PaginationPage<QuestionResponse>()
                .setLimit(limit)
                .setOffset(offset)
                .setTotalRecords(entityList.getTotalElements())
                .setRecords(entityList
                        .getContent()
                        .stream()
                        .map(questionMapper::entityToResponse)
                        .toList());
    }

    @Override
    public PaginationPage<ExamResponse> retrieveExamsByTypeId(Long typeId, Integer offset, Integer limit) {
        final var pageable = new OffsetPageRequest(offset, limit);
        var entity = examRepository.findByExamTypeId(typeId, pageable);
        return new PaginationPage<ExamResponse>()
                .setTotalRecords(entity.getTotalElements())
                .setLimit(limit)
                .setOffset(offset)
                .setRecords(entity
                        .getContent()
                        .stream()
                        .map(examMapper::entityToResponse)
                        .toList());
    }

    @Override
    public Integer submitResponse(Long examId, List<SubmitRequest> requests) {
        final var userRequest = SecurityUtils.requester().orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var examEntity = examRepository.findById(examId).orElseThrow(() -> new ToeicRuntimeException(ErrorCode.ID_NOT_FOUND));
        var questionRelationList = examEntity.getExamQuestionList();
        var questionMap = questionRelationList
                .stream()
                .map(ExamQuestionEntity::getQuestionEntity)
                .collect(Collectors.toMap(IdBase::getId, QuestionEntity::getTrueAnswer));
        int count = 0;
        for (var item : requests) {
            var key = item.getQuestionId();
            var answer = item.getAnswer();
            var trueAnswer = questionMap.get(key);
            if (trueAnswer.equals(answer)) {
                count = count + 1;
            }
        }
        var stringResponse = GsonUtils.arrayToString(requests);
        saveResultExam(userRequest.user(), examId, stringResponse, (double) count);
        return count;
    }

    @Async
    public void saveResultExam(UserEntity user, Long examId, String listAnswer, Double mark){
        var examQuestion = UserAnswerEntity
                .builder()
                .userEntity(user)
                .examId(examId)
                .listAnswer(listAnswer)
                .mark(mark)
                .build();
        userAnswerRepository.save(examQuestion);
    }
}

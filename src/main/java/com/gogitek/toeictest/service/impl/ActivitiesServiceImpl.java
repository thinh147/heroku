package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.pagination.OffsetPageRequest;
import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.response.ExamResponse;
import com.gogitek.toeictest.controller.dto.response.ExamTypeResponse;
import com.gogitek.toeictest.controller.dto.response.QuestionResponse;
import com.gogitek.toeictest.controller.dto.response.VocabGroupResponse;
import com.gogitek.toeictest.entity.ExamEntity;
import com.gogitek.toeictest.mapper.ExamMapper;
import com.gogitek.toeictest.mapper.QuestionMapper;
import com.gogitek.toeictest.mapper.VocabularyMapper;
import com.gogitek.toeictest.repository.ExamRepository;
import com.gogitek.toeictest.repository.ExamTypeRepository;
import com.gogitek.toeictest.repository.GroupRepository;
import com.gogitek.toeictest.repository.QuestionRepository;
import com.gogitek.toeictest.service.ActivitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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


}

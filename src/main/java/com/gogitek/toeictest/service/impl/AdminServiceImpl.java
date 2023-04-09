package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.request.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.*;
import com.gogitek.toeictest.controller.dto.response.*;
import com.gogitek.toeictest.entity.*;
import com.gogitek.toeictest.mapper.ExamMapper;
import com.gogitek.toeictest.mapper.QuestionMapper;
import com.gogitek.toeictest.mapper.UserMapper;
import com.gogitek.toeictest.mapper.VocabularyMapper;
import com.gogitek.toeictest.repository.*;
import com.gogitek.toeictest.service.AdminService;

import javax.transaction.Transactional;

import com.gogitek.toeictest.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    private final VocabularyMapper vocabularyMapper;
    private final GroupRepository groupRepository;
    private final VocabRepository vocabRepository;
    private final ExamMapper examMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final FileRepository fileRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final FileStorageService fileStorageService;

    @Value("${app.base-url}")
    private String baseUrl;

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
            var quantity = typeEntity.getQuantity();
            var questionEntityList = questionRepository.findRandomRecords(quantity);
            insertRelationTable(entity, questionEntityList);
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
        questionRepository.save(entity);
        return questionMapper.entityToAdminResponse(entity);
    }

    @Override
    public VocabularyGroupAdminResponse createVocabularyGroup(CreateVocabularyGroup group) {
        var vocabEntity = vocabularyMapper.groupDtoToEntity(group);
        return vocabularyMapper
                .entityToResponse(
                        groupRepository.save(vocabEntity)
                );
    }

    @Override
    public VocabularyItemResponse createVocabulary(CreateVocabularyRequest request) {
        var groupId = request.getGroupId();
        var groupEntity = groupRepository
                .findById(groupId)
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.ID_NOT_FOUND));
        var item = VocabularyItemEntity
                .builder()
                .word(request.getWord())
                .group(groupEntity)
                .pronunciation(request.getPronunciation())
                .audioPath(request.getAudioPath())
                .description(request.getDescription())
                .build();
        vocabRepository.save(item);
        return VocabularyItemResponse
                .builder()
                .id(item.getId())
                .word(item.getWord())
                .audioPath(item.getAudioPath())
                .description(item.getDescription())
                .pronunciation(item.getPronunciation())
                .build();
    }

    @Override
    public PaginationPage<AdminVocabularyItemResponse> retrieveListItemVocab(VocabItemAdminFilter filter) {
        var pageable = PageRequest.of(filter.getPage(), filter.getSize());
        var word = "";
        if(filter.getWord() != null) {
            word = filter.getWord();
        }
        var page = vocabRepository.findByWordAndGroupId(word, filter.getGroupId(), pageable);
        return new PaginationPage<AdminVocabularyItemResponse>()
                .setLimit(filter.getSize())
                .setOffset(filter.getPage())
                .setTotalRecords(page.getTotalElements())
                .setRecords(page
                        .getContent()
                        .stream()
                        .map(vocabularyMapper::entityToDto)
                        .toList());
    }

    @Override
    public List<VocabularyGroupAdminResponse> retrieveGroupVocabulary() {
        var listVocabGroup = groupRepository.findAll();
        return listVocabGroup
                .stream()
                .map(vocabularyMapper::entityToResponse)
                .toList();
    }

    @Override
    public PaginationPage<UserAdminResponse> retrieveUserForAdminPage(Integer page, Integer size, String name) {
        if(name == null) {
            name = "";
        }
        var pageable = PageRequest.of(page, size);
        var userList = userRepository.retrieveUserListForAdmin(name, pageable);

        return new PaginationPage<UserAdminResponse>()
                .setOffset(page)
                .setLimit(size)
                .setTotalRecords(userList.getTotalElements())
                .setRecords(userList.getContent()
                        .stream()
                        .map(userMapper::entityToAdminResponse)
                        .toList());
    }

    @Override
    public PaginationPage<ExamResponse> retrieveExamsForAdminPage(Integer page, Integer size) {
        var pageable = PageRequest.of(page, size);
        var pageExam = examRepository.findAll(pageable);
        return new PaginationPage<ExamResponse>()
                .setOffset(page)
                .setLimit(size)
                .setTotalRecords(pageExam.getTotalElements())
                .setRecords(pageExam.getContent().stream().map(examMapper::entityToResponse).toList());
    }

    @Override
    public void upload(UploadFileRequest request) {
        if(ObjectUtils.isEmpty(request.getFile())){
            throw new ToeicRuntimeException(ErrorCode.FAILED);
        }
        var fileName = fileStorageService.storeFile(request.getFile());
        var filePath = new StringBuilder(this.baseUrl);
        var entity = FileEntity.builder()
                .fileName(request.getFileName())
                .filePath(filePath.append(fileName).toString())
                .build();
        fileRepository.save(entity);
    }

    @Override
    public List<FileResponse> retrieveFileList(String fileName) {
        return fileRepository
                .findByFileNameLike(fileName)
                .stream()
                .map(item -> FileResponse
                        .builder()
                        .fileName(item.getFileName())
                        .filePath(item.getFilePath())
                        .build()
                ).toList();
    }

    @Transactional
    @Async
    protected void insertRelationTable(final ExamEntity examEntity, List<QuestionEntity> questionList) {
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

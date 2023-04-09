package com.gogitek.toeictest.service;

import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.request.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.*;
import com.gogitek.toeictest.controller.dto.response.*;

import java.util.List;

public interface AdminService {
    void createQuestion(List<CreateQuestionRequest> request);
    void createExam(ExamRequest request);
    PaginationPage<AdminQuestionResponse> retrieveListQuestionAdmin(QuestionFilter filter);
    AdminQuestionResponse modifyQuestion(AdminQuestionResponse response);
    VocabularyGroupAdminResponse createVocabularyGroup(CreateVocabularyGroup group);
    VocabularyItemResponse createVocabulary(CreateVocabularyRequest request);
    PaginationPage<AdminVocabularyItemResponse> retrieveListItemVocab(VocabItemAdminFilter filter);
    List<VocabularyGroupAdminResponse> retrieveGroupVocabulary();
    PaginationPage<UserAdminResponse> retrieveUserForAdminPage(Integer page, Integer size, String name);
    PaginationPage<ExamResponse> retrieveExamsForAdminPage(Integer page, Integer size);
    void upload(UploadFileRequest request);
}

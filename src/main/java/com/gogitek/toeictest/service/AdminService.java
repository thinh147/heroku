package com.gogitek.toeictest.service;

import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.*;
import com.gogitek.toeictest.controller.dto.response.AdminQuestionResponse;
import com.gogitek.toeictest.controller.dto.response.AdminVocabularyItemResponse;
import com.gogitek.toeictest.controller.dto.response.VocabularyGroupAdminResponse;
import com.gogitek.toeictest.controller.dto.response.VocabularyItemResponse;

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
}

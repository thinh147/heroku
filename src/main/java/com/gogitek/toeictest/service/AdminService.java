package com.gogitek.toeictest.service;

import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.CreateQuestionRequest;
import com.gogitek.toeictest.controller.dto.request.QuestionFilter;
import com.gogitek.toeictest.controller.dto.response.AdminQuestionResponse;

import java.util.List;

public interface AdminService {
    void createQuestion(List<CreateQuestionRequest> request);
    void createExam(ExamRequest request);
    PaginationPage<AdminQuestionResponse> retrieveListQuestionAdmin(QuestionFilter filter);
    AdminQuestionResponse modifyQuestion(AdminQuestionResponse response);
}

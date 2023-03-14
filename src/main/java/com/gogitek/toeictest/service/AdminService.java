package com.gogitek.toeictest.service;

import com.gogitek.toeictest.controller.dto.ExamRequest;
import com.gogitek.toeictest.controller.dto.request.CreateQuestionRequest;

import java.util.List;

public interface AdminService {
    void createQuestion(List<CreateQuestionRequest> request);
    void createExam(ExamRequest request);
}

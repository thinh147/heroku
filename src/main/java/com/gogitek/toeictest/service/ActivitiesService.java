package com.gogitek.toeictest.service;

import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.request.SubmitRequest;
import com.gogitek.toeictest.controller.dto.response.ExamResponse;
import com.gogitek.toeictest.controller.dto.response.ExamTypeResponse;
import com.gogitek.toeictest.controller.dto.response.QuestionResponse;
import com.gogitek.toeictest.controller.dto.response.VocabGroupResponse;

import java.util.List;

public interface ActivitiesService {
    PaginationPage<VocabGroupResponse> retrieveGroupPage(Integer offset, Integer limit);
    List<ExamTypeResponse> retrieveExamType();
    PaginationPage<QuestionResponse> retrieveQuestionByExamId(Long examId, Integer offset, Integer limit);
    PaginationPage<ExamResponse> retrieveExamsByTypeId(Long typeId, Integer offset, Integer limit);
    Integer submitResponse(Long examId, List<SubmitRequest> requests);
}

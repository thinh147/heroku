package com.gogitek.toeictest.service;

import com.gogitek.toeictest.config.pagination.PaginationPage;
import com.gogitek.toeictest.controller.dto.response.ExamTypeResponse;
import com.gogitek.toeictest.controller.dto.response.QuestionResponse;
import com.gogitek.toeictest.controller.dto.response.VocabGroupResponse;

import java.util.List;

public interface ActivitiesService {
    PaginationPage<VocabGroupResponse> retrieveGroupPage(Integer offset, Integer limit);
    List<ExamTypeResponse> retrieveExamType();
    PaginationPage<QuestionResponse> retrieveQuestionByTypeId(Long typeId, Integer offset, Integer limit);
}

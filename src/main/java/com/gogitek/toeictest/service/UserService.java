package com.gogitek.toeictest.service;

import com.gogitek.toeictest.controller.dto.response.ExamHistory;
import com.gogitek.toeictest.controller.dto.response.QuestionResult;

import java.util.List;

public interface UserService {
    List<ExamHistory> getListHistory();
    List<QuestionResult> retrieveHistoryDetail(Long historyId);
}

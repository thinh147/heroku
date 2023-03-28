package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.config.utils.GsonUtils;
import com.gogitek.toeictest.controller.dto.request.SubmitRequest;
import com.gogitek.toeictest.controller.dto.response.ExamHistory;
import com.gogitek.toeictest.controller.dto.response.QuestionResult;
import com.gogitek.toeictest.entity.QuestionEntity;
import com.gogitek.toeictest.repository.QuestionRepository;
import com.gogitek.toeictest.repository.UserAnswerRepository;
import com.gogitek.toeictest.security.SecurityUtils;
import com.gogitek.toeictest.service.UserService;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserAnswerRepository userAnswerRepository;
    private final QuestionRepository questionRepository;

    @Override
    public List<ExamHistory> getListHistory() {
        var currentUser = SecurityUtils
                .requester()
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.UNAUTHORIZED));
        var user = currentUser.user();
        var historyExam = userAnswerRepository.findByUserEntity(user);
        return historyExam
                .stream()
                .map(item -> ExamHistory
                        .builder()
                        .id(item.getId())
                        .examId(item.getExamId())
                        .build())
                .toList();
    }

    @Override
    public List<QuestionResult> retrieveHistoryDetail(Long historyId) {
        var history = userAnswerRepository
                .findById(historyId)
                .orElseThrow(() -> new ToeicRuntimeException(ErrorCode.ID_NOT_FOUND));

        var stringAnswer = history.getListAnswer();
        var listAnswer = GsonUtils.stringToArray(stringAnswer, SubmitRequest[].class);
        if(listAnswer == null) {
            return null;
        }
        var map = listAnswer.stream().collect(Collectors.toMap(SubmitRequest::getQuestionId, SubmitRequest::getAnswer));
        var questionList = questionRepository.findByIdIn(map.keySet());
        var res = new ArrayList<QuestionResult>();
        for(var q : questionList){
            var result = QuestionResult
                    .builder()
                    .questionId(q.getId())
                    .answer(map.get(q.getId()))
                    .trueAnswer(q.getTrueAnswer())
                    .isTrue(q.getTrueAnswer().equals(map.get(q.getId())))
                    .build();
            res.add(result);
        }
        return res;
    }
}

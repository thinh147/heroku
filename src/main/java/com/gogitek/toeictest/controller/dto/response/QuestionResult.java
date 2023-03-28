package com.gogitek.toeictest.controller.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResult {
    private Long questionId;
    private String answer;
    private String trueAnswer;
    private Boolean isTrue;
}

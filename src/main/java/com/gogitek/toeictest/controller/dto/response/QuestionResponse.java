package com.gogitek.toeictest.controller.dto.response;

import com.gogitek.toeictest.constant.QuestionType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse {
    private QuestionType type;

    private String detail;

    private String mediaPath;

    private String choiceA;

    private String choiceB;

    private String choiceC;

    private String choiceD;
}

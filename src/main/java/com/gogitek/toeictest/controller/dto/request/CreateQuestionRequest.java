package com.gogitek.toeictest.controller.dto.request;

import com.gogitek.toeictest.constant.Part;
import com.gogitek.toeictest.constant.QuestionType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateQuestionRequest {
    private QuestionType type;

    private String detail;

    private String mediaPath;

    private String choiceA;

    private String choiceB;

    private String choiceC;

    private String choiceD;

    private String trueAnswer;

    private Part part;
}

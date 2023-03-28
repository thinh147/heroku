package com.gogitek.toeictest.controller.dto.response;

import com.gogitek.toeictest.constant.Part;
import com.gogitek.toeictest.constant.QuestionType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminQuestionResponse {
    private Long id;
    private QuestionType type;
    private String detail;
    private String mediaPath;
    private String image;
    private String choiceA;
    private String choiceB;
    private String choiceC;
    private String choiceD;
    private String trueAnswer;
    private Part part;
}

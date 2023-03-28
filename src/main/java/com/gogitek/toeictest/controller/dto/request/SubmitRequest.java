package com.gogitek.toeictest.controller.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitRequest {
    private Long questionId;
    private String answer;
}

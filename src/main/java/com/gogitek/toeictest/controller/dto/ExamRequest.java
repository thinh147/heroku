package com.gogitek.toeictest.controller.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRequest {
    private Long duration;

    private String description;

    private Long typeId;
    private List<Long> questionIds;
}

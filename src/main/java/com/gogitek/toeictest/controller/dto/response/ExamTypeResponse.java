package com.gogitek.toeictest.controller.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamTypeResponse {
    private Long id;
    private String description;
}

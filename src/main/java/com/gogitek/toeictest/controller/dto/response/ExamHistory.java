package com.gogitek.toeictest.controller.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamHistory {
    private Long id;
    private Double mark;
    private Long examId;
    private LocalDateTime createdDate;
}

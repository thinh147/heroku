package com.gogitek.toeictest.controller.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabItemAdminFilter {
    private String word;
    private Long groupId;
    private Integer page = 0;
    private Integer size = 10;
}

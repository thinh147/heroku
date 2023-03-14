package com.gogitek.toeictest.controller.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabGroupResponse {
    private Long id;
    private String groupName;
    private String groupImage;
    private List<VocabularyItemResponse> items;
}

package com.gogitek.toeictest.controller.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminVocabularyItemResponse {
    private Long id;
    private String word;
    private String pronunciation;
    private String audioPath;
    private String description;
    private VocabularyGroupAdminResponse group;
}

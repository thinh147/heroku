package com.gogitek.toeictest.controller.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyItemResponse {
    private String word;
    private String pronunciation;
    private String audioPath;
    private String description;
}

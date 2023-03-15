package com.gogitek.toeictest.controller.dto.request;

import com.gogitek.toeictest.controller.dto.response.VocabularyGroupAdminResponse;
import com.gogitek.toeictest.entity.VocabularyGroupEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateVocabularyRequest {
    private String word;
    private String pronunciation;
    private String audioPath;
    private String description;
    private Long groupId;
}

package com.gogitek.toeictest.controller.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VocabularyGroupAdminResponse {
    private Long id;
    private String groupName;
    private String groupImage;
}

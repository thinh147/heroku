package com.gogitek.toeictest.controller.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateVocabularyGroup {
    public String groupName;
    public String groupImage;
}

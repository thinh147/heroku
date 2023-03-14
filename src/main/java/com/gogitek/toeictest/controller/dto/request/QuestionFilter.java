package com.gogitek.toeictest.controller.dto.request;

import com.gogitek.toeictest.constant.Part;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionFilter {
    private Integer offset = 0;
    private Integer limit = 10;
    private List<Part> parts = new ArrayList<>();
}
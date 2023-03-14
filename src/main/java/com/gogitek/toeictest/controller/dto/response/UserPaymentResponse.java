package com.gogitek.toeictest.controller.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPaymentResponse {
    private Long userId;
    private String name;
}

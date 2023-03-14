package com.gogitek.toeictest.controller.dto.response;


import com.gogitek.toeictest.constant.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Double cost;
    private LocalDateTime createdDate;
    private PaymentStatus status;
    private UserPaymentResponse userInfo;
}

package com.gogitek.toeictest.controller.dto.request;

import com.gogitek.toeictest.constant.Period;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePaymentRequest {
    private Period period;
}

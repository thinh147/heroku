package com.gogitek.toeictest.controller.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull(message = "You must enter phone number")
    private String phoneNumber;

    @NotNull(message = "You must enter password")
    private String password;

    private String fullName;
}

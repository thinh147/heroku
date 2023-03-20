package com.gogitek.toeictest.controller.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Long expiryDuration;

    private UserProfiles profiles;

    public LoginResponse(String accessToken, String refreshToken, Long expiryDuration, UserProfiles profiles) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiryDuration = expiryDuration;
        tokenType = "Bearer ";
        this.profiles = profiles;
    }
}
package com.gogitek.toeictest.service;

import com.gogitek.toeictest.controller.dto.request.LoginRequest;
import com.gogitek.toeictest.controller.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    LoginResponse register(LoginRequest loginRequest);
}

package com.gogitek.toeictest.controller;

import com.gogitek.toeictest.config.response.ResponseEntityBuilder;
import com.gogitek.toeictest.controller.dto.request.LoginRequest;
import com.gogitek.toeictest.controller.route.AuthRoute;
import com.gogitek.toeictest.controller.route.BaseRoute;
import com.gogitek.toeictest.service.AuthService;
import com.gogitek.toeictest.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BaseRoute.BASE_ENDPOINT + BaseRoute.AUTH_BASE)
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping(AuthRoute.LOGIN_PATH)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(authService.login(request))
                .build();
    }

    @PostMapping(AuthRoute.SIGN_UP_PATH)
    public ResponseEntity<?> signup(@RequestBody LoginRequest request) {
        return ResponseEntityBuilder
                .getBuilder()
                .setDetails(authService.register(request))
                .build();
    }
}

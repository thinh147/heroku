package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import com.gogitek.toeictest.constant.Level;
import com.gogitek.toeictest.constant.Roles;
import com.gogitek.toeictest.controller.dto.request.LoginRequest;
import com.gogitek.toeictest.controller.dto.response.LoginResponse;
import com.gogitek.toeictest.entity.UserEntity;
import com.gogitek.toeictest.repository.UserRepository;
import com.gogitek.toeictest.security.custom.JwtTokenUtil;
import com.gogitek.toeictest.service.AuthService;
import com.gogitek.toeictest.service.RefreshTokenService;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getPhoneNumber(),
                            loginRequest.getPassword()
                    )
            );
            var token = jwtTokenUtil.generateToken(loginRequest.getPhoneNumber());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var refreshTokenEntity = refreshTokenService.createRefreshToken();
            refreshTokenService.save(refreshTokenEntity);
            return new LoginResponse(token, refreshTokenEntity.getToken(), jwtTokenUtil.getDuration());
        } catch (Exception e) {
            throw new ToeicRuntimeException(ErrorCode.USER_NAME_PASSWORD_NOT_MATCH);
        }
    }

    @Override
    @Transactional
    public LoginResponse register(LoginRequest loginRequest) {
        var existed = userRepository.existsByUsername(loginRequest.getPhoneNumber());
        if (existed) {
            throw new ToeicRuntimeException(ErrorCode.ALREADY_EXIST);
        }
        var entity = UserEntity
                .builder()
                .username(loginRequest.getPhoneNumber())
                .phoneNumber(loginRequest.getPhoneNumber())
                .password(passwordEncoder.encode(loginRequest.getPassword()))
                .role(Roles.USER)
                .level(Level.TRIAL)
                .lastName(loginRequest.getFullName())
                .build();
        userRepository.save(entity);
        var token = this.jwtTokenUtil.generateToken(entity.getUsername());
        var refreshTokenEntity = refreshTokenService.createRefreshToken();
        refreshTokenService.save(refreshTokenEntity);
        return new LoginResponse(token, refreshTokenEntity.getToken(), jwtTokenUtil.getDuration());
    }
}

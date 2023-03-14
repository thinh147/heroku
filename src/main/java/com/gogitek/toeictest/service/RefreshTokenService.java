package com.gogitek.toeictest.service;


import com.gogitek.toeictest.entity.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshTokenEntity> findByToken(String token);

    RefreshTokenEntity save(RefreshTokenEntity refreshTokenEntity);

    RefreshTokenEntity createRefreshToken();

    void verifyExpiration(RefreshTokenEntity token);

    void deleteById(Long id);

    void increaseCount(RefreshTokenEntity refreshTokenEntity);
}

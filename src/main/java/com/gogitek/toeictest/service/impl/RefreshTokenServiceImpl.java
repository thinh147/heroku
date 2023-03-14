package com.gogitek.toeictest.service.impl;

import com.gogitek.toeictest.config.exception.TokenRefreshException;
import com.gogitek.toeictest.entity.RefreshTokenEntity;
import com.gogitek.toeictest.repository.RefreshTokenRepository;
import com.gogitek.toeictest.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public Optional<RefreshTokenEntity> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public RefreshTokenEntity save(RefreshTokenEntity refreshTokenEntity) {
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Override
    public RefreshTokenEntity createRefreshToken() {
        var refreshTokenEntity = new RefreshTokenEntity();
        refreshTokenEntity.setExpiryDate(Instant.now().plusMillis(86400000));
        refreshTokenEntity.setToken(UUID.randomUUID().toString());
        refreshTokenEntity.setRefreshCount(0L);
        return refreshTokenEntity;
    }

    @Override
    public void verifyExpiration(RefreshTokenEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new TokenRefreshException(token.getToken(), "Expired token. Please issue a new request");
        }
    }

    @Override
    public void deleteById(Long id) {
        refreshTokenRepository.deleteById(id);
    }

    @Override
    public void increaseCount(RefreshTokenEntity refreshTokenEntity) {
        refreshTokenEntity.incrementRefreshCount();
        save(refreshTokenEntity);
    }
}

package com.gogitek.toeictest.security.custom;

import com.gogitek.toeictest.config.exception.ErrorCode;
import com.gogitek.toeictest.config.exception.ToeicRuntimeException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private final static Logger Logger = LoggerFactory.getLogger(JwtTokenUtil.class);

    @Value("${app.auth.tokenSecret}")
    private String jwtSecret;

    @Value("${app.auth.tokenExpirationMsec}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            Logger.error("Invalid JWT signature: {}", e.getMessage());
            throw new ToeicRuntimeException(ErrorCode.SIGNATURE_NOT_CORRECT);
        } catch (MalformedJwtException e) {
            Logger.error("Invalid JWT Token: {}", e.getMessage());
            throw new ToeicRuntimeException(ErrorCode.AUTHORIZATION_FIELD_MISSING);
        } catch (ExpiredJwtException e) {
            Logger.error("Invalid JWT expired: {}", e.getMessage());
            throw new ToeicRuntimeException(ErrorCode.EXPIRED);
        } catch (UnsupportedJwtException e) {
            Logger.error("Invalid JWT unsupported: {}", e.getMessage());
            throw new ToeicRuntimeException(ErrorCode.UN_SUPPORT_FILE_EXTENSION);
        } catch (IllegalArgumentException e) {
            Logger.error("JWT claims string empty : {}", e.getMessage());
            throw new ToeicRuntimeException(ErrorCode.JWT_CLAIM_EMPTY);
        }
    }

    public long getDuration() {
        return this.jwtExpirationMs;
    }
}

package com.nd2k.authenticationapi.utils;

import com.nd2k.authenticationapi.model.auth.RefreshToken;
import com.nd2k.authenticationapi.model.auth.User;
import com.nd2k.authenticationapi.model.exception.JwtException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtils {

    private final String accessTokenSecret;
    private final String refreshTokenSecret;
    private final long accessTokenExpirationMs;
    private final long refreshTokenExpirationMs;

    private static final String APPLICATION_NAME = "my_auth_api";

    public JwtUtils(
            @Value("${security.accessToken.secret}") String accessTokenSecret,
            @Value("${security.refreshToken.secret}") String refreshTokenSecret,
            @Value("${security.accessToken.expirationMinutes}") long accessTokenExpirationMinutes,
            @Value("${security.refreshToken.expirationDays}") long refreshTokenExpirationDays) {
        this.accessTokenSecret = accessTokenSecret;
        this.refreshTokenSecret = refreshTokenSecret;
        this.accessTokenExpirationMs = accessTokenExpirationMinutes * 1000 * 60;
        this.refreshTokenExpirationMs = refreshTokenExpirationDays * 1000 * 60 * 60 * 24;
    }

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setIssuer(APPLICATION_NAME)
                .setSubject(user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + accessTokenExpirationMs))
                .signWith(SignatureAlgorithm.HS512, accessTokenSecret)
                .compact();
    }

    public String generateRefreshToken(User user, RefreshToken refreshToken) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenId", refreshToken.getId());
        return Jwts.builder()
                .setIssuer(APPLICATION_NAME)
                .setSubject(user.getId())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + refreshTokenExpirationMs))
                .signWith(SignatureAlgorithm.HS512, refreshTokenSecret)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parser()
                    .setSigningKey(accessTokenSecret)
                    .parseClaimsJws(accessToken);
            return true;
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new JwtException(e.getMessage());
        }
    }
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jwts.parser()
                    .setSigningKey(refreshTokenSecret)
                    .parseClaimsJws(refreshToken);
            return true;
        } catch (SignatureException |
                 MalformedJwtException |
                 ExpiredJwtException |
                 UnsupportedJwtException |
                 IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new JwtException(e.getMessage());
        }
    }


    public String getUserIdFromAccessToken(String accessToken) {
        return Jwts.parser()
                .setSigningKey(accessTokenSecret)
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();
    }

    public String getUserIdFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(refreshTokenSecret)
                .parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
    }

    public String getTokenIdFromRefreshToken(String refreshToken) {
        return Jwts.parser()
                .setSigningKey(refreshTokenSecret)
                .parseClaimsJws(refreshToken)
                .getBody()
                .get("tokenId", String.class);
    }
}

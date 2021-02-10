package com.example.userservice.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.userservice.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${app.jwt.secret.token}")
    private String JWT_SECRET_TOKEN;

    @Value("${app.jwt.expirationInMs.token}")
    private long JWT_EXPIRATION_TOKEN;

    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setHeader(header())
                .setClaims(getClaims(user))
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(getTimeNow())
                .setExpiration(getExpiredTokenTime())
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_TOKEN)
                .compact();
    }

    public String generateRefreshToken(User user) {
        String id = Long.toString(user.getId());
        return Jwts.builder()
                .setClaims(generateRandomString())
                .setSubject(id)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_TOKEN)
                .compact();
    }

    public boolean verifyToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_TOKEN).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private Date getTimeNow() {
        return new Date();
    }

    public int getIdFromSubjectJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET_TOKEN)
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    private Date getExpiredTokenTime() {
        return new Date(getTimeNow().getTime() + JWT_EXPIRATION_TOKEN);
    }

    private Map<String, Object> header() {
        Map<String, Object> map = new HashMap<>();
        map.put("typ", "JWT");
        return map;
    }

    private Map<String, Object> generateRandomString() {
        Map<String, Object> mClaims = new HashMap<>();
        String randomString = RandomStringUtils.random(40, true, true);
        mClaims.put("info", randomString);
        return mClaims;
    }

    private Map<String, Object> getClaims(User user) {
        Map<String, Object> mClaims = new HashMap<>();
        mClaims.put("email", user.getEmail());
        mClaims.put("role", user.getRole());
        mClaims.put("uid", user.getUid());

        return mClaims;
    }
}

package com.example.nexacron.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;

import java.util.Base64;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @PostConstruct
    public void init() {
        // JWT 설정 초기화
        if (secret == null) {
            secret = "default-secret-key";
        }
    }

    public String generateToken(String email) {
        // 간단한 Base64 인코딩 (실제 프로젝트에서는 JWT 라이브러리 사용)
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = "{\"sub\":\"" + email + "\"}";

        String encodedHeader = Base64.getEncoder().encodeToString(header.getBytes());
        String encodedPayload = Base64.getEncoder().encodeToString(payload.getBytes());

        // 간단한 서명
        String signature = String.valueOf(encodedHeader.length() + encodedPayload.length());

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public String getEmailFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new RuntimeException("Invalid token format");
            }

            String payload = new String(Base64.getDecoder().decode(parts[1]));
            return payload.split(":")[3].replace("\"", "").replace("}", "");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse token");
        }
    }

    public boolean validateToken(String token) {
        try {
            getEmailFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        // 간단한 만료 확인
        return false; // 실제 구현에서는 만료 시간 확인
    }
}
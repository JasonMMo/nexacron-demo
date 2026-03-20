package com.example.nexacron.controller;

import com.example.nexacron.dto.LoginRequest;
import com.example.nexacron.dto.UserRequest;
import com.example.nexacron.model.User;
import com.example.nexacron.service.AuthService;
import com.example.nexacron.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<User> signup(@RequestBody UserRequest userRequest) {
        User user = authService.register(userRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@RequestHeader("Authorization") String authorizationHeader) {
        // Bearer 토큰에서 이메일 추출
        String token = authorizationHeader.replace("Bearer ", "");
        String email = jwtUtil.getEmailFromToken(token);

        User user = authService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
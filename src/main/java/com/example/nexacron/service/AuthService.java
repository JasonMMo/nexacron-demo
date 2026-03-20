package com.example.nexacron.service;

import com.example.nexacron.dto.LoginRequest;
import com.example.nexacron.dto.UserRequest;
import com.example.nexacron.model.User;
import com.example.nexacron.repository.UserRepository;
import com.example.nexacron.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    // 간단한 비밀번호 인코더 (실제 프로젝트에서는 bcrypt 사용)
    private SimplePasswordEncoder passwordEncoder = new SimplePasswordEncoder();

    public User register(UserRequest userRequest) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // User 엔티티 생성
        User user = new User();
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setName(userRequest.getName());

        // 저장
        return userRepository.save(user);
    }

    public String login(LoginRequest loginRequest) {
        // 이메일로 사용자 찾기
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        User user = userOptional.get();

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // JWT 토큰 생성
        return jwtUtil.generateToken(user.getEmail(), user.getId());
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }

    // 간단한 PasswordEncoder 구현
    private static class SimplePasswordEncoder {
        public String encode(CharSequence rawPassword) {
            return "encoded_" + rawPassword;
        }

        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return encodedPassword.equals("encoded_" + rawPassword);
        }
    }
}
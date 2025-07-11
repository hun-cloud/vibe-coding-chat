package com.example.demo.application.service;

import com.example.demo.application.dto.AuthResponse;
import com.example.demo.application.dto.LoginRequest;
import com.example.demo.application.dto.RegisterRequest;
import com.example.demo.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // 사용자명 중복 확인
        if (userService.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다");
        }
        
        // 이메일 중복 확인
        if (userService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다");
        }
        
        // 새 사용자 생성
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .nickname(request.getNickname())
                .role(User.UserRole.USER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        User savedUser = userService.saveUser(user);
        
        // JWT 토큰 생성
        String accessToken = jwtService.generateAccessToken(
                savedUser.getUsername(), 
                savedUser.getId(), 
                savedUser.getRole().name()
        );
        String refreshToken = jwtService.generateRefreshToken(
                savedUser.getUsername(), 
                savedUser.getId()
        );
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600L) // 1시간
                .user(AuthResponse.UserDto.builder()
                        .id(savedUser.getId())
                        .username(savedUser.getUsername())
                        .email(savedUser.getEmail())
                        .nickname(savedUser.getNickname())
                        .profileImage(savedUser.getProfileImage())
                        .role(savedUser.getRole().name())
                        .build())
                .build();
    }
    
    public AuthResponse login(LoginRequest request) {
        // 인증 시도
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        
        // 인증된 사용자 정보 가져오기
        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        
        // JWT 토큰 생성
        String accessToken = jwtService.generateAccessToken(
                user.getUsername(), 
                user.getId(), 
                user.getRole().name()
        );
        String refreshToken = jwtService.generateRefreshToken(
                user.getUsername(), 
                user.getId()
        );
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(3600L) // 1시간
                .user(AuthResponse.UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .profileImage(user.getProfileImage())
                        .role(user.getRole().name())
                        .build())
                .build();
    }
    
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다");
        }
        
        String username = jwtService.extractUsername(refreshToken);
        Long userId = jwtService.extractUserId(refreshToken);
        
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));
        
        // 새로운 액세스 토큰 생성
        String newAccessToken = jwtService.generateAccessToken(
                user.getUsername(), 
                user.getId(), 
                user.getRole().name()
        );
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken) // 기존 리프레시 토큰 재사용
                .expiresIn(3600L)
                .user(AuthResponse.UserDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .profileImage(user.getProfileImage())
                        .role(user.getRole().name())
                        .build())
                .build();
    }
} 
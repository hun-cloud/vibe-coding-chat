package com.example.demo.adapter.in.web;

import com.example.demo.application.dto.AuthResponse;
import com.example.demo.application.dto.CommonResponse;
import com.example.demo.application.dto.LoginRequest;
import com.example.demo.application.dto.RegisterRequest;
import com.example.demo.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<CommonResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("회원가입 요청: {}", request.getUsername());
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(CommonResponse.success(response, "회원가입이 완료되었습니다"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("로그인 요청: {}", request.getUsername());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(CommonResponse.success(response, "로그인이 완료되었습니다"));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<CommonResponse<AuthResponse>> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest()
                    .body(CommonResponse.error("유효하지 않은 토큰입니다", "INVALID_TOKEN"));
        }
        
        String refreshToken = authHeader.substring(7);
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(CommonResponse.success(response, "토큰이 갱신되었습니다"));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<CommonResponse<Void>> logout() {
        // JWT는 stateless이므로 클라이언트에서 토큰을 삭제하면 됩니다
        // 필요시 블랙리스트에 토큰을 추가할 수 있습니다
        return ResponseEntity.ok(CommonResponse.success("로그아웃이 완료되었습니다"));
    }
} 
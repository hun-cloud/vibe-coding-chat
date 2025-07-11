package com.example.demo.application.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {
    
    @Builder.Default
    private boolean success = true;
    
    @Builder.Default
    private String message = "요청이 성공적으로 처리되었습니다.";
    
    private T data;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private String errorCode;
    
    // 성공 응답 생성 메서드
    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .success(true)
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .build();
    }
    
    public static <T> CommonResponse<T> success(T data, String message) {
        return CommonResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
    
    public static CommonResponse<Void> success() {
        return CommonResponse.<Void>builder()
                .success(true)
                .message("요청이 성공적으로 처리되었습니다.")
                .build();
    }
    
    public static CommonResponse<Void> success(String message) {
        return CommonResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();
    }
    
    // 실패 응답 생성 메서드
    public static <T> CommonResponse<T> error(String message) {
        return CommonResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
    
    public static <T> CommonResponse<T> error(String message, String errorCode) {
        return CommonResponse.<T>builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
    
    public static <T> CommonResponse<T> error(String message, T data) {
        return CommonResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .build();
    }
} 
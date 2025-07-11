package com.example.demo.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    
    @NotNull(message = "채팅방 ID는 필수입니다")
    private Long roomId;
    
    @NotBlank(message = "메시지 내용은 필수입니다")
    @Size(min = 1, max = 1000, message = "메시지는 1-1000자 사이여야 합니다")
    private String content;
    
    @Builder.Default
    private String messageType = "TEXT";
} 
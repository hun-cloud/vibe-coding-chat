package com.example.demo.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinChatRoomRequest {
    
    @NotNull(message = "채팅방 ID는 필수입니다")
    private Long roomId;
} 
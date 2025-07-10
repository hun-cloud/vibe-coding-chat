package com.example.demo.application.dto;

import jakarta.validation.constraints.NotNull;

public class JoinChatRoomRequest {
    
    @NotNull(message = "채팅방 ID는 필수입니다")
    private Long roomId;
    
    // Getters and Setters
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
} 
package com.example.demo.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SendMessageRequest {
    
    @NotNull(message = "채팅방 ID는 필수입니다")
    private Long roomId;
    
    @NotBlank(message = "메시지 내용은 필수입니다")
    @Size(min = 1, max = 1000, message = "메시지는 1-1000자 사이여야 합니다")
    private String content;
    
    private String messageType = "TEXT";
    
    // Getters and Setters
    public Long getRoomId() { return roomId; }
    public void setRoomId(Long roomId) { this.roomId = roomId; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
} 
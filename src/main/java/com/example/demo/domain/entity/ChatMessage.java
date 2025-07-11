package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {
    
    private String id;
    private Long roomId;
    private Long senderId;
    private String content;
    
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;
    
    private String senderName;
    private String senderProfileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private Boolean isDeleted = false;
    
    // Business logic methods
    public void delete() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void updateContent(String newContent) {
        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isTextMessage() {
        return MessageType.TEXT.equals(messageType);
    }
    
    public boolean isSystemMessage() {
        return MessageType.SYSTEM.equals(messageType);
    }
    
    public enum MessageType {
        TEXT, IMAGE, FILE, SYSTEM, EMOJI
    }
} 
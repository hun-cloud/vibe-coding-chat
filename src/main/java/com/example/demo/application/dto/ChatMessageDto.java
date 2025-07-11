package com.example.demo.application.dto;

import com.example.demo.domain.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {
    
    private String id;
    private Long roomId;
    private Long senderId;
    private String content;
    private String messageType;
    private String senderName;
    private String senderProfileImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDeleted;
    
    // Constructor from domain entity
    public static ChatMessageDto fromDomain(ChatMessage message) {
        return ChatMessageDto.builder()
                .id(message.getId())
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .messageType(message.getMessageType() != null ? message.getMessageType().name() : null)
                .senderName(message.getSenderName())
                .senderProfileImage(message.getSenderProfileImage())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .isDeleted(message.getIsDeleted())
                .build();
    }
} 
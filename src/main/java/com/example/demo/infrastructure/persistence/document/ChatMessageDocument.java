package com.example.demo.infrastructure.persistence.document;

import com.example.demo.domain.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessageDocument {
    
    @Id
    private String id;
    
    @Indexed
    private Long roomId;
    
    @Indexed
    private Long senderId;
    
    @TextIndexed
    private String content;
    
    @Indexed
    @Builder.Default
    private ChatMessage.MessageType messageType = ChatMessage.MessageType.TEXT;
    
    private String senderName;
    private String senderProfileImage;
    
    @Indexed(expireAfterSeconds = 2592000) // 30 days
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private Boolean isDeleted = false;
    
    // Mapping methods
    public ChatMessage toDomain() {
        return ChatMessage.builder()
                .id(this.id)
                .roomId(this.roomId)
                .senderId(this.senderId)
                .content(this.content)
                .messageType(this.messageType)
                .senderName(this.senderName)
                .senderProfileImage(this.senderProfileImage)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .isDeleted(this.isDeleted)
                .build();
    }
    
    public static ChatMessageDocument fromDomain(ChatMessage message) {
        return ChatMessageDocument.builder()
                .id(message.getId())
                .roomId(message.getRoomId())
                .senderId(message.getSenderId())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .senderName(message.getSenderName())
                .senderProfileImage(message.getSenderProfileImage())
                .createdAt(message.getCreatedAt())
                .updatedAt(message.getUpdatedAt())
                .isDeleted(message.getIsDeleted())
                .build();
    }
} 
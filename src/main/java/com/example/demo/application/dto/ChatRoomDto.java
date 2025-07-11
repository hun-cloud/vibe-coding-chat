package com.example.demo.application.dto;

import com.example.demo.domain.entity.ChatRoom;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    
    private Long id;
    private String name;
    private String description;
    private String roomType;
    private String ottType;
    private Integer maxParticipants;
    private Boolean isPrivate;
    private Boolean isActive;
    private Long createdBy;
    private String createdByUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer memberCount;
    private List<ChatRoomMemberDto> members;
    
    // Constructor from domain entity
    public static ChatRoomDto fromDomain(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .description(chatRoom.getDescription())
                .roomType(chatRoom.getRoomType() != null ? chatRoom.getRoomType().name() : null)
                .ottType(chatRoom.getOttType() != null ? chatRoom.getOttType().name() : null)
                .maxParticipants(chatRoom.getMaxParticipants())
                .isPrivate(chatRoom.getIsPrivate())
                .isActive(chatRoom.getIsActive())
                .createdBy(chatRoom.getCreatedBy() != null ? chatRoom.getCreatedBy().getId() : null)
                .createdByUsername(chatRoom.getCreatedBy() != null ? chatRoom.getCreatedBy().getUsername() : null)
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
    }
} 
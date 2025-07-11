package com.example.demo.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom {
    
    private Long id;
    private String name;
    private String description;
    private RoomType roomType;
    private OttType ottType;
    private Integer maxParticipants;
    private Boolean isPrivate = false;
    private Boolean isActive = true;
    private User createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    @Builder.Default
    private Set<ChatRoomMember> members = new HashSet<>();
    
    // Business logic methods
    public void addMember(ChatRoomMember member) {
        if (maxParticipants != null && members.size() >= maxParticipants) {
            throw new IllegalStateException("Chat room is full");
        }
        members.add(member);
        updatedAt = LocalDateTime.now();
    }
    
    public void removeMember(ChatRoomMember member) {
        members.remove(member);
        updatedAt = LocalDateTime.now();
    }
    
    public boolean isMember(User user) {
        return members.stream()
                .anyMatch(member -> member.getUser().getId().equals(user.getId()));
    }
    
    public enum RoomType {
        GENERAL, OTT, PRIVATE, GROUP
    }
    
    public enum OttType {
        NETFLIX, DISNEY_PLUS, WAVVE, TVING, WATCHA, APPLE_TV, AMAZON_PRIME
    }
} 
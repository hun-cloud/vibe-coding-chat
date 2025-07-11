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
public class ChatRoomMember {
    
    private Long id;
    private ChatRoom chatRoom;
    private User user;
    
    @Builder.Default
    private MemberRole role = MemberRole.MEMBER;
    
    private LocalDateTime joinedAt;
    private LocalDateTime lastReadAt;
    
    @Builder.Default
    private Boolean isActive = true;
    
    // Business logic methods
    public void updateLastRead() {
        this.lastReadAt = LocalDateTime.now();
    }
    
    public boolean isOwner() {
        return MemberRole.OWNER.equals(role);
    }
    
    public boolean isAdmin() {
        return MemberRole.ADMIN.equals(role) || MemberRole.OWNER.equals(role);
    }
    
    public boolean isModerator() {
        return MemberRole.MODERATOR.equals(role) || MemberRole.ADMIN.equals(role) || MemberRole.OWNER.equals(role);
    }
    
    public enum MemberRole {
        OWNER, ADMIN, MODERATOR, MEMBER
    }
} 
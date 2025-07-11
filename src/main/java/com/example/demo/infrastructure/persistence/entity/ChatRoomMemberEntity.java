package com.example.demo.infrastructure.persistence.entity;

import com.example.demo.domain.entity.ChatRoomMember;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_room_members")
public class ChatRoomMemberEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoom;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    @Builder.Default
    private ChatRoomMember.MemberRole role = ChatRoomMember.MemberRole.MEMBER;
    
    @Column(name = "joined_at")
    private LocalDateTime joinedAt;
    
    @Column(name = "last_read_at")
    private LocalDateTime lastReadAt;
    
    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;
    
    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
        lastReadAt = LocalDateTime.now();
    }
    
    // Mapping methods
    public ChatRoomMember toDomain() {
        return ChatRoomMember.builder()
                .id(this.id)
                .chatRoom(this.chatRoom != null ? this.chatRoom.toDomain() : null)
                .user(this.user != null ? this.user.toDomain() : null)
                .role(this.role)
                .joinedAt(this.joinedAt)
                .lastReadAt(this.lastReadAt)
                .isActive(this.isActive)
                .build();
    }
    
    public static ChatRoomMemberEntity fromDomain(ChatRoomMember member) {
        return ChatRoomMemberEntity.builder()
                .id(member.getId())
                .chatRoom(member.getChatRoom() != null ? ChatRoomEntity.fromDomain(member.getChatRoom()) : null)
                .user(member.getUser() != null ? UserEntity.fromDomain(member.getUser()) : null)
                .role(member.getRole())
                .joinedAt(member.getJoinedAt())
                .lastReadAt(member.getLastReadAt())
                .isActive(member.getIsActive())
                .build();
    }
} 
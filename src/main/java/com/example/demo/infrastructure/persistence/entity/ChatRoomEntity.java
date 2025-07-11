package com.example.demo.infrastructure.persistence.entity;

import com.example.demo.domain.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "chat_rooms")
public class ChatRoomEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private ChatRoom.RoomType roomType;
    
    @Column(name = "ott_type")
    @Enumerated(EnumType.STRING)
    private ChatRoom.OttType ottType;
    
    @Column(name = "max_participants")
    private Integer maxParticipants;
    
    @Column(name = "is_private")
    private Boolean isPrivate = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<ChatRoomMemberEntity> members = new HashSet<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Mapping methods
    public ChatRoom toDomain() {
        ChatRoom chatRoom = ChatRoom.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .roomType(this.roomType)
                .ottType(this.ottType)
                .maxParticipants(this.maxParticipants)
                .isPrivate(this.isPrivate)
                .isActive(this.isActive)
                .createdBy(this.createdBy != null ? this.createdBy.toDomain() : null)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
        
        if (this.members != null) {
            Set<com.example.demo.domain.entity.ChatRoomMember> domainMembers = this.members.stream()
                    .map(ChatRoomMemberEntity::toDomain)
                    .collect(Collectors.toSet());
            chatRoom.setMembers(domainMembers);
        }
        
        return chatRoom;
    }
    
    public static ChatRoomEntity fromDomain(ChatRoom chatRoom) {
        ChatRoomEntity entity = ChatRoomEntity.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .description(chatRoom.getDescription())
                .roomType(chatRoom.getRoomType())
                .ottType(chatRoom.getOttType())
                .maxParticipants(chatRoom.getMaxParticipants())
                .isPrivate(chatRoom.getIsPrivate())
                .isActive(chatRoom.getIsActive())
                .createdBy(chatRoom.getCreatedBy() != null ? UserEntity.fromDomain(chatRoom.getCreatedBy()) : null)
                .createdAt(chatRoom.getCreatedAt())
                .updatedAt(chatRoom.getUpdatedAt())
                .build();
        
        if (chatRoom.getMembers() != null) {
            Set<ChatRoomMemberEntity> entityMembers = chatRoom.getMembers().stream()
                    .map(ChatRoomMemberEntity::fromDomain)
                    .collect(Collectors.toSet());
            entity.setMembers(entityMembers);
        }
        
        return entity;
    }
} 
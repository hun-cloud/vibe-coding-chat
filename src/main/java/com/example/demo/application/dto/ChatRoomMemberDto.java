package com.example.demo.application.dto;

import com.example.demo.domain.entity.ChatRoomMember;
import java.time.LocalDateTime;

public class ChatRoomMemberDto {
    
    private Long id;
    private Long userId;
    private String username;
    private String nickname;
    private String profileImage;
    private String role;
    private LocalDateTime joinedAt;
    private LocalDateTime lastReadAt;
    private Boolean isActive;
    
    // Constructors
    public ChatRoomMemberDto() {}
    
    public ChatRoomMemberDto(ChatRoomMember member) {
        this.id = member.getId();
        this.userId = member.getUser().getId();
        this.username = member.getUser().getUsername();
        this.nickname = member.getUser().getNickname();
        this.profileImage = member.getUser().getProfileImage();
        this.role = member.getRole() != null ? member.getRole().name() : null;
        this.joinedAt = member.getJoinedAt();
        this.lastReadAt = member.getLastReadAt();
        this.isActive = member.getIsActive();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
    
    public LocalDateTime getLastReadAt() { return lastReadAt; }
    public void setLastReadAt(LocalDateTime lastReadAt) { this.lastReadAt = lastReadAt; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
} 
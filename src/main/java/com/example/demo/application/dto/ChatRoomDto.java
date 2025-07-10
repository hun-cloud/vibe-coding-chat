package com.example.demo.application.dto;

import com.example.demo.domain.entity.ChatRoom;
import java.time.LocalDateTime;
import java.util.List;

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
    
    // Constructors
    public ChatRoomDto() {}
    
    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.name = chatRoom.getName();
        this.description = chatRoom.getDescription();
        this.roomType = chatRoom.getRoomType() != null ? chatRoom.getRoomType().name() : null;
        this.ottType = chatRoom.getOttType() != null ? chatRoom.getOttType().name() : null;
        this.maxParticipants = chatRoom.getMaxParticipants();
        this.isPrivate = chatRoom.getIsPrivate();
        this.isActive = chatRoom.getIsActive();
        this.createdBy = chatRoom.getCreatedBy() != null ? chatRoom.getCreatedBy().getId() : null;
        this.createdByUsername = chatRoom.getCreatedBy() != null ? chatRoom.getCreatedBy().getUsername() : null;
        this.createdAt = chatRoom.getCreatedAt();
        this.updatedAt = chatRoom.getUpdatedAt();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    
    public String getOttType() { return ottType; }
    public void setOttType(String ottType) { this.ottType = ottType; }
    
    public Integer getMaxParticipants() { return maxParticipants; }
    public void setMaxParticipants(Integer maxParticipants) { this.maxParticipants = maxParticipants; }
    
    public Boolean getIsPrivate() { return isPrivate; }
    public void setIsPrivate(Boolean isPrivate) { this.isPrivate = isPrivate; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    
    public String getCreatedByUsername() { return createdByUsername; }
    public void setCreatedByUsername(String createdByUsername) { this.createdByUsername = createdByUsername; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public Integer getMemberCount() { return memberCount; }
    public void setMemberCount(Integer memberCount) { this.memberCount = memberCount; }
    
    public List<ChatRoomMemberDto> getMembers() { return members; }
    public void setMembers(List<ChatRoomMemberDto> members) { this.members = members; }
} 
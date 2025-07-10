package com.example.demo.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateChatRoomRequest {
    
    @NotBlank(message = "채팅방 이름은 필수입니다")
    @Size(min = 1, max = 100, message = "채팅방 이름은 1-100자 사이여야 합니다")
    private String name;
    
    @Size(max = 500, message = "설명은 500자 이하여야 합니다")
    private String description;
    
    @NotNull(message = "채팅방 타입은 필수입니다")
    private String roomType;
    
    private String ottType;
    
    private Integer maxParticipants;
    
    private Boolean isPrivate = false;
    
    // Getters and Setters
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
} 
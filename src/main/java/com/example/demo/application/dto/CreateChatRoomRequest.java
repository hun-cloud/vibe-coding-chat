package com.example.demo.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    
    @Builder.Default
    private Boolean isPrivate = false;
} 
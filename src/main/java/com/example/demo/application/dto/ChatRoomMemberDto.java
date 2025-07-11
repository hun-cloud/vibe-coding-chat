package com.example.demo.application.dto;

import com.example.demo.domain.entity.ChatRoomMember;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    
    // Constructor from domain entity
    public static ChatRoomMemberDto fromDomain(ChatRoomMember member) {
        return ChatRoomMemberDto.builder()
                .id(member.getId())
                .userId(member.getUser().getId())
                .username(member.getUser().getUsername())
                .nickname(member.getUser().getNickname())
                .profileImage(member.getUser().getProfileImage())
                .role(member.getRole() != null ? member.getRole().name() : null)
                .joinedAt(member.getJoinedAt())
                .lastReadAt(member.getLastReadAt())
                .isActive(member.getIsActive())
                .build();
    }
} 
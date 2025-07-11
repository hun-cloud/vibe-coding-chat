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
public class User {
    
    private Long id;
    private String username;
    private String password;
    private String email;
    private String nickname;
    private String profileImage;
    
    @Builder.Default
    private UserRole role = UserRole.USER;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Business logic methods
    public void updateProfile(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.updatedAt = LocalDateTime.now();
    }
    
    public boolean isAdmin() {
        return UserRole.ADMIN.equals(role);
    }
    
    public boolean isModerator() {
        return UserRole.MODERATOR.equals(role) || UserRole.ADMIN.equals(role);
    }
    
    public enum UserRole {
        USER, ADMIN, MODERATOR
    }
} 
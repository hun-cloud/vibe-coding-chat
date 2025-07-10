package com.example.demo.infrastructure.config;

import com.example.demo.application.service.ChatRoomService;
import com.example.demo.application.service.UserService;
import com.example.demo.domain.entity.ChatRoom;
import com.example.demo.domain.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final ChatRoomService chatRoomService;

    public DataInitializer(UserService userService, ChatRoomService chatRoomService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }

    @Override
    public void run(String... args) throws Exception {
        // 기본 사용자 생성
        try {
            User adminUser = userService.createUser("admin", "password", "admin@example.com");
            System.out.println("기본 사용자 생성됨: " + adminUser.getUsername());
        } catch (Exception e) {
            System.out.println("기본 사용자 이미 존재함");
        }

        // OTT별 기본 채팅방 생성
        createDefaultChatRooms();
    }

    private void createDefaultChatRooms() {
        try {
            // Netflix 채팅방
            createChatRoom("Netflix 채팅방", "Netflix 컨텐츠에 대한 이야기를 나누는 공간입니다.", "OTT", "NETFLIX");
            
            // Disney+ 채팅방
            createChatRoom("Disney+ 채팅방", "Disney+ 컨텐츠에 대한 이야기를 나누는 공간입니다.", "OTT", "DISNEY_PLUS");
            
            // Wavve 채팅방
            createChatRoom("Wavve 채팅방", "Wavve 컨텐츠에 대한 이야기를 나누는 공간입니다.", "OTT", "WAVVE");
            
            // Tving 채팅방
            createChatRoom("Tving 채팅방", "Tving 컨텐츠에 대한 이야기를 나누는 공간입니다.", "OTT", "TVING");
            
            // Watcha 채팅방
            createChatRoom("Watcha 채팅방", "Watcha 컨텐츠에 대한 이야기를 나누는 공간입니다.", "OTT", "WATCHA");
            
            // 일반 채팅방
            createChatRoom("일반 채팅방", "자유롭게 대화를 나누는 공간입니다.", "GENERAL", null);
            
            System.out.println("기본 채팅방들이 생성되었습니다.");
        } catch (Exception e) {
            System.out.println("채팅방 생성 중 오류: " + e.getMessage());
        }
    }

    private void createChatRoom(String name, String description, String roomType, String ottType) {
        try {
            com.example.demo.application.dto.CreateChatRoomRequest request = new com.example.demo.application.dto.CreateChatRoomRequest();
            request.setName(name);
            request.setDescription(description);
            request.setRoomType(roomType);
            request.setOttType(ottType);
            request.setMaxParticipants(100);
            request.setIsPrivate(false);
            
            chatRoomService.createChatRoom(request, 1L); // admin 사용자 ID
        } catch (Exception e) {
            System.out.println("채팅방 생성 실패: " + name + " - " + e.getMessage());
        }
    }
} 
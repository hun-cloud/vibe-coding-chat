package com.example.demo.application.port.in;

import com.example.demo.application.dto.ChatRoomDto;
import com.example.demo.application.dto.CreateChatRoomRequest;
import com.example.demo.application.dto.JoinChatRoomRequest;
import com.example.demo.domain.entity.ChatRoom;

import java.util.List;

public interface ChatRoomUseCase {
    
    ChatRoomDto createChatRoom(CreateChatRoomRequest request, Long userId);
    
    ChatRoomDto getChatRoom(Long roomId);
    
    List<ChatRoomDto> getChatRoomsByOttType(String ottType);
    
    List<ChatRoomDto> getPublicChatRooms();
    
    List<ChatRoomDto> getUserChatRooms(Long userId);
    
    void joinChatRoom(JoinChatRoomRequest request, Long userId);
    
    void leaveChatRoom(Long roomId, Long userId);
    
    void deleteChatRoom(Long roomId, Long userId);
    
    boolean isUserMemberOfRoom(Long roomId, Long userId);
} 
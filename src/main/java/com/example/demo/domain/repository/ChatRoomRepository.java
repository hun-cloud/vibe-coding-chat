package com.example.demo.domain.repository;

import com.example.demo.domain.entity.ChatRoom;
import com.example.demo.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    
    ChatRoom save(ChatRoom chatRoom);
    
    Optional<ChatRoom> findById(Long id);
    
    List<ChatRoom> findAll();
    
    List<ChatRoom> findByCreatedBy(User user);
    
    List<ChatRoom> findByRoomType(ChatRoom.RoomType roomType);
    
    List<ChatRoom> findByOttType(ChatRoom.OttType ottType);
    
    List<ChatRoom> findActiveRooms();
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
} 
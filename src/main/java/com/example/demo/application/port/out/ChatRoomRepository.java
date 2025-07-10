package com.example.demo.application.port.out;

import com.example.demo.domain.entity.ChatRoom;
import com.example.demo.domain.entity.ChatRoomMember;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    
    ChatRoom save(ChatRoom chatRoom);
    
    Optional<ChatRoom> findById(Long id);
    
    List<ChatRoom> findByOttType(String ottType);
    
    List<ChatRoom> findPublicRooms();
    
    List<ChatRoom> findByUserId(Long userId);
    
    void deleteById(Long id);
    
    boolean existsById(Long id);
    
    // ChatRoomMember operations
    ChatRoomMember saveMember(ChatRoomMember member);
    
    Optional<ChatRoomMember> findMemberByRoomIdAndUserId(Long roomId, Long userId);
    
    List<ChatRoomMember> findMembersByRoomId(Long roomId);
    
    void deleteMemberByRoomIdAndUserId(Long roomId, Long userId);
    
    boolean isUserMemberOfRoom(Long roomId, Long userId);
} 
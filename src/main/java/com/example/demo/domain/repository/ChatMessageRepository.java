package com.example.demo.domain.repository;

import com.example.demo.domain.entity.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository {
    
    ChatMessage save(ChatMessage chatMessage);
    
    Optional<ChatMessage> findById(String id);
    
    List<ChatMessage> findByRoomId(Long roomId);
    
    List<ChatMessage> findByRoomIdAndCreatedAtAfter(Long roomId, LocalDateTime after);
    
    List<ChatMessage> findBySenderId(Long senderId);
    
    List<ChatMessage> findRecentMessages(Long roomId, int limit);
    
    void deleteById(String id);
    
    void deleteByRoomId(Long roomId);
    
    long countByRoomId(Long roomId);
} 
package com.example.demo.application.port.out;

import com.example.demo.domain.document.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository {
    
    ChatMessage save(ChatMessage chatMessage);
    
    Optional<ChatMessage> findById(String id);
    
    List<ChatMessage> findByRoomIdOrderByCreatedAtDesc(Long roomId, int page, int size);
    
    List<ChatMessage> findByRoomIdAndCreatedAtBetween(Long roomId, LocalDateTime start, LocalDateTime end);
    
    void deleteById(String id);
    
    void deleteByRoomId(Long roomId);
    
    long countByRoomId(Long roomId);
    
    List<ChatMessage> findUnreadMessages(Long roomId, LocalDateTime lastReadAt);
    
    void deleteOldMessages(LocalDateTime before);
} 
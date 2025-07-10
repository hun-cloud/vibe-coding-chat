package com.example.demo.application.port.out;

import com.example.demo.domain.document.ChatMessage;

public interface ChatMessagePublisher {
    
    void publishMessage(Long roomId, ChatMessage message);
    
    void publishUserJoined(Long roomId, Long userId, String username);
    
    void publishUserLeft(Long roomId, Long userId, String username);
    
    void publishSystemMessage(Long roomId, String content);
} 
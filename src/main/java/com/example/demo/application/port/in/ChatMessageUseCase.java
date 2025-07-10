package com.example.demo.application.port.in;

import com.example.demo.application.dto.ChatMessageDto;
import com.example.demo.application.dto.SendMessageRequest;

import java.util.List;

public interface ChatMessageUseCase {
    
    ChatMessageDto sendMessage(SendMessageRequest request, Long userId);
    
    List<ChatMessageDto> getChatMessages(Long roomId, int page, int size);
    
    List<ChatMessageDto> getChatMessagesByDate(Long roomId, String date);
    
    void deleteMessage(String messageId, Long userId);
    
    void markAsRead(Long roomId, Long userId);
    
    int getUnreadMessageCount(Long roomId, Long userId);
} 
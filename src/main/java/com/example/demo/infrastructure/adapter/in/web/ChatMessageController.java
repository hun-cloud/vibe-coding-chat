package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.dto.ChatMessageDto;
import com.example.demo.application.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms/{roomId}/messages")
@CrossOrigin(origins = "*")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        List<ChatMessageDto> messages = chatMessageService.getChatMessages(roomId, page, size);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ChatMessageDto>> getChatMessagesByDate(
            @PathVariable Long roomId,
            @PathVariable String date) {
        
        List<ChatMessageDto> messages = chatMessageService.getChatMessagesByDate(roomId, date);
        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable Long roomId,
            @PathVariable String messageId) {
        
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatMessageService.deleteMessage(messageId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatMessageService.markAsRead(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Integer> getUnreadMessageCount(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        int count = chatMessageService.getUnreadMessageCount(roomId, userId);
        return ResponseEntity.ok(count);
    }
} 
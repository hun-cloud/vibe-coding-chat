package com.example.demo.adapter.in.web;

import com.example.demo.application.dto.ChatMessageDto;
import com.example.demo.application.dto.CommonResponse;
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
    public ResponseEntity<CommonResponse<List<ChatMessageDto>>> getChatMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        
        List<ChatMessageDto> messages = chatMessageService.getChatMessages(roomId, page, size);
        return ResponseEntity.ok(CommonResponse.success(messages, "채팅 메시지를 성공적으로 조회했습니다."));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<CommonResponse<List<ChatMessageDto>>> getChatMessagesByDate(
            @PathVariable Long roomId,
            @PathVariable String date) {
        
        List<ChatMessageDto> messages = chatMessageService.getChatMessagesByDate(roomId, date);
        return ResponseEntity.ok(CommonResponse.success(messages, date + " 날짜의 메시지를 성공적으로 조회했습니다."));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<CommonResponse<Void>> deleteMessage(
            @PathVariable Long roomId,
            @PathVariable String messageId) {
        
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatMessageService.deleteMessage(messageId, userId);
        return ResponseEntity.ok(CommonResponse.success("메시지가 성공적으로 삭제되었습니다."));
    }

    @PostMapping("/mark-read")
    public ResponseEntity<CommonResponse<Void>> markAsRead(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatMessageService.markAsRead(roomId, userId);
        return ResponseEntity.ok(CommonResponse.success("메시지를 읽음으로 표시했습니다."));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<CommonResponse<Integer>> getUnreadMessageCount(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        int count = chatMessageService.getUnreadMessageCount(roomId, userId);
        return ResponseEntity.ok(CommonResponse.success(count, "읽지 않은 메시지 수를 성공적으로 조회했습니다."));
    }
} 
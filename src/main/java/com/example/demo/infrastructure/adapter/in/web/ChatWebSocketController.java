package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.dto.SendMessageRequest;
import com.example.demo.application.service.ChatMessageService;
import com.example.demo.application.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    public ChatWebSocketController(ChatMessageService chatMessageService,
                                 ChatRoomService chatRoomService,
                                 SimpMessagingTemplate messagingTemplate,
                                 ObjectMapper objectMapper) {
        this.chatMessageService = chatMessageService;
        this.chatRoomService = chatRoomService;
        this.messagingTemplate = messagingTemplate;
        this.objectMapper = objectMapper;
    }

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public String handleChatMessage(@Payload String message, String roomId, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // 임시로 사용자 ID를 1로 설정 (실제로는 인증에서 가져와야 함)
            Long userId = 1L;
            
            // JSON 메시지를 SendMessageRequest로 변환
            Map<String, Object> messageMap = objectMapper.readValue(message, Map.class);
            
            SendMessageRequest request = new SendMessageRequest();
            request.setRoomId(Long.valueOf(roomId));
            request.setContent((String) messageMap.get("content"));
            request.setMessageType((String) messageMap.getOrDefault("messageType", "TEXT"));
            
            // 메시지 전송
            chatMessageService.sendMessage(request, userId);
            
            return message;
        } catch (Exception e) {
            // 에러 메시지 반환
            return createErrorMessage(e.getMessage());
        }
    }

    @MessageMapping("/join/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public String handleJoinRoom(@Payload String message, String roomId, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // 임시로 사용자 ID를 1로 설정
            Long userId = 1L;
            
            // 채팅방 참여
            chatRoomService.joinChatRoom(new com.example.demo.application.dto.JoinChatRoomRequest() {{
                setRoomId(Long.valueOf(roomId));
            }}, userId);
            
            // 사용자 입장 메시지 전송
            chatMessageService.sendUserJoinedMessage(Long.valueOf(roomId), userId, "User_" + userId);
            
            return createSystemMessage("사용자가 입장했습니다.");
        } catch (Exception e) {
            return createErrorMessage(e.getMessage());
        }
    }

    @MessageMapping("/leave/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public String handleLeaveRoom(@Payload String message, String roomId, SimpMessageHeaderAccessor headerAccessor) {
        try {
            // 임시로 사용자 ID를 1로 설정
            Long userId = 1L;
            
            // 사용자 퇴장 메시지 전송
            chatMessageService.sendUserLeftMessage(Long.valueOf(roomId), userId, "User_" + userId);
            
            // 채팅방 나가기
            chatRoomService.leaveChatRoom(Long.valueOf(roomId), userId);
            
            return createSystemMessage("사용자가 퇴장했습니다.");
        } catch (Exception e) {
            return createErrorMessage(e.getMessage());
        }
    }

    private String createSystemMessage(String content) {
        try {
            Map<String, Object> systemMessage = Map.of(
                "type", "SYSTEM",
                "content", content,
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return objectMapper.writeValueAsString(systemMessage);
        } catch (Exception e) {
            return "{\"type\":\"ERROR\",\"content\":\"시스템 메시지 생성 실패\"}";
        }
    }

    private String createErrorMessage(String errorMessage) {
        try {
            Map<String, Object> errorResponse = Map.of(
                "type", "ERROR",
                "content", errorMessage,
                "timestamp", java.time.LocalDateTime.now().toString()
            );
            return objectMapper.writeValueAsString(errorResponse);
        } catch (Exception e) {
            return "{\"type\":\"ERROR\",\"content\":\"에러 메시지 생성 실패\"}";
        }
    }
} 
package com.example.demo.infrastructure.adapter.out.redis;

import com.example.demo.application.port.out.ChatMessagePublisher;
import com.example.demo.domain.document.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisChatMessagePublisher implements ChatMessagePublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisChatMessagePublisher(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publishMessage(Long roomId, ChatMessage message) {
        try {
            String channel = "chat:room:" + roomId;
            String messageJson = objectMapper.writeValueAsString(message);
            redisTemplate.convertAndSend(channel, messageJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메시지 직렬화 실패", e);
        }
    }

    @Override
    public void publishUserJoined(Long roomId, Long userId, String username) {
        try {
            String channel = "chat:room:" + roomId + ":events";
            UserEvent event = new UserEvent("USER_JOINED", userId, username);
            String eventJson = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(channel, eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("이벤트 직렬화 실패", e);
        }
    }

    @Override
    public void publishUserLeft(Long roomId, Long userId, String username) {
        try {
            String channel = "chat:room:" + roomId + ":events";
            UserEvent event = new UserEvent("USER_LEFT", userId, username);
            String eventJson = objectMapper.writeValueAsString(event);
            redisTemplate.convertAndSend(channel, eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("이벤트 직렬화 실패", e);
        }
    }

    @Override
    public void publishSystemMessage(Long roomId, String content) {
        try {
            String channel = "chat:room:" + roomId + ":system";
            SystemMessage systemMessage = new SystemMessage(content);
            String messageJson = objectMapper.writeValueAsString(systemMessage);
            redisTemplate.convertAndSend(channel, messageJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("시스템 메시지 직렬화 실패", e);
        }
    }

    // 내부 클래스들
    public static class UserEvent {
        private String type;
        private Long userId;
        private String username;

        public UserEvent() {}

        public UserEvent(String type, Long userId, String username) {
            this.type = type;
            this.userId = userId;
            this.username = username;
        }

        // Getters and Setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }

    public static class SystemMessage {
        private String content;
        private String timestamp;

        public SystemMessage() {}

        public SystemMessage(String content) {
            this.content = content;
            this.timestamp = java.time.LocalDateTime.now().toString();
        }

        // Getters and Setters
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public String getTimestamp() { return timestamp; }
        public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
    }
} 
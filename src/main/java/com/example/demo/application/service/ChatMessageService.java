package com.example.demo.application.service;

import com.example.demo.application.dto.ChatMessageDto;
import com.example.demo.application.dto.SendMessageRequest;
import com.example.demo.application.port.in.ChatMessageUseCase;
import com.example.demo.application.port.out.ChatMessagePublisher;
import com.example.demo.application.port.out.ChatMessageRepository;
import com.example.demo.application.port.out.ChatRoomRepository;
import com.example.demo.domain.entity.ChatMessage;
import com.example.demo.domain.entity.ChatRoomMember;
import com.example.demo.domain.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatMessageService implements ChatMessageUseCase {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessagePublisher chatMessagePublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                            ChatMessagePublisher chatMessagePublisher,
                            ChatRoomRepository chatRoomRepository,
                            UserService userService) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessagePublisher = chatMessagePublisher;
        this.chatRoomRepository = chatRoomRepository;
        this.userService = userService;
    }

    @Override
    public ChatMessageDto sendMessage(SendMessageRequest request, Long userId) {
        // 사용자와 채팅방 멤버십 확인
        User user = userService.getUserById(userId);
        if (!chatRoomRepository.isUserMemberOfRoom(request.getRoomId(), userId)) {
            throw new RuntimeException("채팅방 멤버가 아닙니다.");
        }

        // 메시지 생성
        ChatMessage message = ChatMessage.builder()
                .roomId(request.getRoomId())
                .senderId(userId)
                .content(request.getContent())
                .messageType(ChatMessage.MessageType.valueOf(request.getMessageType()))
                .senderName(user.getNickname() != null ? user.getNickname() : user.getUsername())
                .senderProfileImage(user.getProfileImage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 메시지 저장
        ChatMessage savedMessage = chatMessageRepository.save(message);

        // Redis를 통해 메시지 발행
        chatMessagePublisher.publishMessage(request.getRoomId(), savedMessage);

        return ChatMessageDto.fromDomain(savedMessage);
    }

    @Override
    public List<ChatMessageDto> getChatMessages(Long roomId, int page, int size) {
        // 채팅방 존재 확인
        if (!chatRoomRepository.existsById(roomId)) {
            throw new RuntimeException("채팅방을 찾을 수 없습니다: " + roomId);
        }

        List<ChatMessage> messages = chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(roomId, page, size);
        
        return messages.stream()
                .map(ChatMessageDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatMessageDto> getChatMessagesByDate(Long roomId, String date) {
        // 채팅방 존재 확인
        if (!chatRoomRepository.existsById(roomId)) {
            throw new RuntimeException("채팅방을 찾을 수 없습니다: " + roomId);
        }

        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDay = targetDate.atStartOfDay();
        LocalDateTime endOfDay = targetDate.atTime(23, 59, 59);

        List<ChatMessage> messages = chatMessageRepository.findByRoomIdAndCreatedAtBetween(roomId, startOfDay, endOfDay);
        
        return messages.stream()
                .map(ChatMessageDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(String messageId, Long userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다: " + messageId));

        // 메시지 작성자만 삭제 가능
        if (!message.getSenderId().equals(userId)) {
            throw new RuntimeException("메시지 작성자만 삭제할 수 있습니다.");
        }

        message.setIsDeleted(true);
        chatMessageRepository.save(message);
    }

    @Override
    public void markAsRead(Long roomId, Long userId) {
        // 채팅방 멤버십 확인
        if (!chatRoomRepository.isUserMemberOfRoom(roomId, userId)) {
            throw new RuntimeException("채팅방 멤버가 아닙니다.");
        }

        // 마지막 읽은 시간 업데이트
        ChatRoomMember member = chatRoomRepository.findMemberByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new RuntimeException("채팅방 멤버를 찾을 수 없습니다."));
        
        member.setLastReadAt(LocalDateTime.now());
        chatRoomRepository.saveMember(member);
    }

    @Override
    public int getUnreadMessageCount(Long roomId, Long userId) {
        // 채팅방 멤버십 확인
        if (!chatRoomRepository.isUserMemberOfRoom(roomId, userId)) {
            return 0;
        }

        // 마지막 읽은 시간 조회
        ChatRoomMember member = chatRoomRepository.findMemberByRoomIdAndUserId(roomId, userId)
                .orElse(null);
        
        if (member == null || member.getLastReadAt() == null) {
            // 마지막 읽은 시간이 없으면 전체 메시지 수 반환
            return (int) chatMessageRepository.countByRoomId(roomId);
        }

        // 읽지 않은 메시지 조회
        List<ChatMessage> unreadMessages = chatMessageRepository.findUnreadMessages(roomId, member.getLastReadAt());
        return unreadMessages.size();
    }

            // 시스템 메시지 전송
        public void sendSystemMessage(Long roomId, String content) {
            ChatMessage systemMessage = ChatMessage.builder()
                    .roomId(roomId)
                    .senderId(0L) // 시스템 사용자 ID
                    .content(content)
                    .messageType(ChatMessage.MessageType.SYSTEM)
                    .senderName("시스템")
                    .senderProfileImage(null)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

        ChatMessage savedMessage = chatMessageRepository.save(systemMessage);
        chatMessagePublisher.publishSystemMessage(roomId, content);
    }

    // 사용자 입장 메시지
    public void sendUserJoinedMessage(Long roomId, Long userId, String username) {
        chatMessagePublisher.publishUserJoined(roomId, userId, username);
        sendSystemMessage(roomId, username + "님이 입장하셨습니다.");
    }

    // 사용자 퇴장 메시지
    public void sendUserLeftMessage(Long roomId, Long userId, String username) {
        chatMessagePublisher.publishUserLeft(roomId, userId, username);
        sendSystemMessage(roomId, username + "님이 퇴장하셨습니다.");
    }
} 
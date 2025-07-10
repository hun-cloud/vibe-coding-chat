package com.example.demo.application.service;

import com.example.demo.application.dto.ChatMessageDto;
import com.example.demo.application.dto.SendMessageRequest;
import com.example.demo.application.port.out.ChatMessagePublisher;
import com.example.demo.application.port.out.ChatMessageRepository;
import com.example.demo.application.port.out.ChatRoomRepository;
import com.example.demo.domain.document.ChatMessage;
import com.example.demo.domain.entity.ChatRoomMember;
import com.example.demo.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatMessagePublisher chatMessagePublisher;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatMessageService chatMessageService;

    private User testUser;
    private ChatMessage testMessage;
    private ChatRoomMember testMember;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setNickname("테스트유저");

        testMessage = new ChatMessage();
        testMessage.setId("msg1");
        testMessage.setRoomId(1L);
        testMessage.setSenderId(1L);
        testMessage.setContent("테스트 메시지");
        testMessage.setMessageType(ChatMessage.MessageType.TEXT);
        testMessage.setSenderName("테스트유저");
        testMessage.setCreatedAt(LocalDateTime.now());

        testMember = new ChatRoomMember();
        testMember.setId(1L);
        testMember.setUser(testUser);
        testMember.setLastReadAt(LocalDateTime.now().minusHours(1));
    }

    @Test
    void sendMessage_성공() {
        // given
        SendMessageRequest request = new SendMessageRequest();
        request.setRoomId(1L);
        request.setContent("안녕하세요");
        request.setMessageType("TEXT");

        when(userService.getUserById(1L)).thenReturn(testUser);
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(true);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(testMessage);

        // when
        ChatMessageDto result = chatMessageService.sendMessage(request, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("테스트 메시지");
        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(chatMessagePublisher).publishMessage(1L, testMessage);
    }

    @Test
    void sendMessage_채팅방멤버아님_예외발생() {
        // given
        SendMessageRequest request = new SendMessageRequest();
        request.setRoomId(1L);
        request.setContent("안녕하세요");

        when(userService.getUserById(1L)).thenReturn(testUser);
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> chatMessageService.sendMessage(request, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("채팅방 멤버가 아닙니다");
    }

    @Test
    void getChatMessages_성공() {
        // given
        List<ChatMessage> messages = Arrays.asList(testMessage);
        when(chatRoomRepository.existsById(1L)).thenReturn(true);
        when(chatMessageRepository.findByRoomIdOrderByCreatedAtDesc(1L, 0, 50)).thenReturn(messages);

        // when
        List<ChatMessageDto> result = chatMessageService.getChatMessages(1L, 0, 50);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("테스트 메시지");
    }

    @Test
    void getChatMessages_채팅방없음_예외발생() {
        // given
        when(chatRoomRepository.existsById(999L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> chatMessageService.getChatMessages(999L, 0, 50))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("채팅방을 찾을 수 없습니다");
    }

    @Test
    void deleteMessage_성공() {
        // given
        when(chatMessageRepository.findById("msg1")).thenReturn(Optional.of(testMessage));

        // when
        chatMessageService.deleteMessage("msg1", 1L);

        // then
        verify(chatMessageRepository).save(any(ChatMessage.class));
        assertThat(testMessage.getIsDeleted()).isTrue();
    }

    @Test
    void deleteMessage_다른사용자메시지_예외발생() {
        // given
        testMessage.setSenderId(2L); // 다른 사용자의 메시지
        when(chatMessageRepository.findById("msg1")).thenReturn(Optional.of(testMessage));

        // when & then
        assertThatThrownBy(() -> chatMessageService.deleteMessage("msg1", 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("메시지 작성자만 삭제할 수 있습니다");
    }

    @Test
    void markAsRead_성공() {
        // given
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(true);
        when(chatRoomRepository.findMemberByRoomIdAndUserId(1L, 1L)).thenReturn(Optional.of(testMember));

        // when
        chatMessageService.markAsRead(1L, 1L);

        // then
        verify(chatRoomRepository).saveMember(testMember);
        assertThat(testMember.getLastReadAt()).isAfter(LocalDateTime.now().minusMinutes(1));
    }

    @Test
    void getUnreadMessageCount_성공() {
        // given
        List<ChatMessage> unreadMessages = Arrays.asList(testMessage);
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(true);
        when(chatRoomRepository.findMemberByRoomIdAndUserId(1L, 1L)).thenReturn(Optional.of(testMember));
        when(chatMessageRepository.findUnreadMessages(1L, testMember.getLastReadAt())).thenReturn(unreadMessages);

        // when
        int result = chatMessageService.getUnreadMessageCount(1L, 1L);

        // then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void getUnreadMessageCount_멤버아님_0반환() {
        // given
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(false);

        // when
        int result = chatMessageService.getUnreadMessageCount(1L, 1L);

        // then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void sendSystemMessage_성공() {
        // given
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(testMessage);

        // when
        chatMessageService.sendSystemMessage(1L, "시스템 메시지");

        // then
        verify(chatMessageRepository).save(any(ChatMessage.class));
        verify(chatMessagePublisher).publishSystemMessage(1L, "시스템 메시지");
    }
} 
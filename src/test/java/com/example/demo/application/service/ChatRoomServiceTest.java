package com.example.demo.application.service;

import com.example.demo.application.dto.ChatRoomDto;
import com.example.demo.application.dto.CreateChatRoomRequest;
import com.example.demo.application.dto.JoinChatRoomRequest;
import com.example.demo.application.port.out.ChatRoomRepository;
import com.example.demo.domain.entity.ChatRoom;
import com.example.demo.domain.entity.ChatRoomMember;
import com.example.demo.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ChatRoomService chatRoomService;

    private User testUser;
    private ChatRoom testChatRoom;
    private ChatRoomMember testMember;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        testChatRoom = new ChatRoom();
        testChatRoom.setId(1L);
        testChatRoom.setName("테스트 채팅방");
        testChatRoom.setDescription("테스트용 채팅방");
        testChatRoom.setRoomType(ChatRoom.RoomType.GENERAL);
        testChatRoom.setCreatedBy(testUser);

        testMember = new ChatRoomMember(testChatRoom, testUser);
        testMember.setId(1L);
        testMember.setRole(ChatRoomMember.MemberRole.OWNER);
    }

    @Test
    void createChatRoom_성공() {
        // given
        CreateChatRoomRequest request = new CreateChatRoomRequest();
        request.setName("새 채팅방");
        request.setDescription("새로운 채팅방");
        request.setRoomType("GENERAL");
        request.setIsPrivate(false);

        when(userService.getUserById(1L)).thenReturn(testUser);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(testChatRoom);
        when(chatRoomRepository.saveMember(any(ChatRoomMember.class))).thenReturn(testMember);

        // when
        ChatRoomDto result = chatRoomService.createChatRoom(request, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("테스트 채팅방");
        verify(chatRoomRepository).save(any(ChatRoom.class));
        verify(chatRoomRepository).saveMember(any(ChatRoomMember.class));
    }

    @Test
    void getChatRoom_성공() {
        // given
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(testChatRoom));
        when(chatRoomRepository.findMembersByRoomId(1L)).thenReturn(Arrays.asList(testMember));

        // when
        ChatRoomDto result = chatRoomService.getChatRoom(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("테스트 채팅방");
        assertThat(result.getMembers()).hasSize(1);
    }

    @Test
    void getChatRoom_채팅방없음_예외발생() {
        // given
        when(chatRoomRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> chatRoomService.getChatRoom(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("채팅방을 찾을 수 없습니다");
    }

    @Test
    void getPublicChatRooms_성공() {
        // given
        List<ChatRoom> chatRooms = Arrays.asList(testChatRoom);
        when(chatRoomRepository.findPublicRooms()).thenReturn(chatRooms);

        // when
        List<ChatRoomDto> result = chatRoomService.getPublicChatRooms();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("테스트 채팅방");
    }

    @Test
    void joinChatRoom_성공() {
        // given
        JoinChatRoomRequest request = new JoinChatRoomRequest();
        request.setRoomId(1L);

        when(userService.getUserById(1L)).thenReturn(testUser);
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(testChatRoom));
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(false);
        when(chatRoomRepository.findMembersByRoomId(1L)).thenReturn(Arrays.asList());
        when(chatRoomRepository.saveMember(any(ChatRoomMember.class))).thenReturn(testMember);

        // when
        chatRoomService.joinChatRoom(request, 1L);

        // then
        verify(chatRoomRepository).saveMember(any(ChatRoomMember.class));
    }

    @Test
    void joinChatRoom_이미멤버_예외발생() {
        // given
        JoinChatRoomRequest request = new JoinChatRoomRequest();
        request.setRoomId(1L);

        when(userService.getUserById(1L)).thenReturn(testUser);
        when(chatRoomRepository.findById(1L)).thenReturn(Optional.of(testChatRoom));
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> chatRoomService.joinChatRoom(request, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("이미 채팅방에 참여 중입니다");
    }

    @Test
    void leaveChatRoom_성공() {
        // given
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(true);

        // when
        chatRoomService.leaveChatRoom(1L, 1L);

        // then
        verify(chatRoomRepository).deleteMemberByRoomIdAndUserId(1L, 1L);
    }

    @Test
    void leaveChatRoom_멤버아님_예외발생() {
        // given
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> chatRoomService.leaveChatRoom(1L, 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("채팅방 멤버가 아닙니다");
    }

    @Test
    void isUserMemberOfRoom_성공() {
        // given
        when(chatRoomRepository.isUserMemberOfRoom(1L, 1L)).thenReturn(true);

        // when
        boolean result = chatRoomService.isUserMemberOfRoom(1L, 1L);

        // then
        assertThat(result).isTrue();
    }
} 
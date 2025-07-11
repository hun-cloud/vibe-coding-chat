package com.example.demo.application.service;

import com.example.demo.application.dto.ChatRoomDto;
import com.example.demo.application.dto.ChatRoomMemberDto;
import com.example.demo.application.dto.CreateChatRoomRequest;
import com.example.demo.application.dto.JoinChatRoomRequest;
import com.example.demo.application.port.in.ChatRoomUseCase;
import com.example.demo.application.port.out.ChatRoomRepository;
import com.example.demo.domain.entity.ChatRoom;
import com.example.demo.domain.entity.ChatRoomMember;
import com.example.demo.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatRoomService implements ChatRoomUseCase {

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public ChatRoomService(ChatRoomRepository chatRoomRepository, UserService userService) {
        this.chatRoomRepository = chatRoomRepository;
        this.userService = userService;
    }

    @Override
    public ChatRoomDto createChatRoom(CreateChatRoomRequest request, Long userId) {
        User user = userService.getUserById(userId);
        
        ChatRoom chatRoom = ChatRoom.builder()
                .name(request.getName())
                .description(request.getDescription())
                .roomType(ChatRoom.RoomType.valueOf(request.getRoomType()))
                .ottType(request.getOttType() != null ? ChatRoom.OttType.valueOf(request.getOttType()) : null)
                .maxParticipants(request.getMaxParticipants())
                .isPrivate(request.getIsPrivate())
                .createdBy(user)
                .build();
        
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        
        // 채팅방 생성자를 멤버로 추가
        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoom(savedChatRoom)
                .user(user)
                .role(ChatRoomMember.MemberRole.OWNER)
                .build();
        chatRoomRepository.saveMember(member);
        
        return ChatRoomDto.fromDomain(savedChatRoom);
    }

    @Override
    public ChatRoomDto getChatRoom(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다: " + roomId));
        
        ChatRoomDto dto = ChatRoomDto.fromDomain(chatRoom);
        
        // 멤버 정보 추가
        List<ChatRoomMember> members = chatRoomRepository.findMembersByRoomId(roomId);
        List<ChatRoomMemberDto> memberDtos = members.stream()
                .map(ChatRoomMemberDto::fromDomain)
                .collect(Collectors.toList());
        dto.setMembers(memberDtos);
        dto.setMemberCount(memberDtos.size());
        
        return dto;
    }

    @Override
    public List<ChatRoomDto> getChatRoomsByOttType(String ottType) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByOttType(ottType);
        return chatRooms.stream()
                .map(ChatRoomDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomDto> getPublicChatRooms() {
        List<ChatRoom> chatRooms = chatRoomRepository.findPublicRooms();
        return chatRooms.stream()
                .map(ChatRoomDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ChatRoomDto> getUserChatRooms(Long userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId);
        return chatRooms.stream()
                .map(ChatRoomDto::fromDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void joinChatRoom(JoinChatRoomRequest request, Long userId) {
        User user = userService.getUserById(userId);
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다: " + request.getRoomId()));
        
        // 이미 멤버인지 확인
        if (chatRoomRepository.isUserMemberOfRoom(request.getRoomId(), userId)) {
            throw new RuntimeException("이미 채팅방에 참여 중입니다.");
        }
        
        // 최대 참여자 수 확인
        List<ChatRoomMember> members = chatRoomRepository.findMembersByRoomId(request.getRoomId());
        if (chatRoom.getMaxParticipants() != null && members.size() >= chatRoom.getMaxParticipants()) {
            throw new RuntimeException("채팅방이 가득 찼습니다.");
        }
        
        ChatRoomMember member = ChatRoomMember.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatRoomRepository.saveMember(member);
    }

    @Override
    public void leaveChatRoom(Long roomId, Long userId) {
        if (!chatRoomRepository.isUserMemberOfRoom(roomId, userId)) {
            throw new RuntimeException("채팅방 멤버가 아닙니다.");
        }
        
        chatRoomRepository.deleteMemberByRoomIdAndUserId(roomId, userId);
    }

    @Override
    public void deleteChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다: " + roomId));
        
        // 소유자만 삭제 가능
        ChatRoomMember member = chatRoomRepository.findMemberByRoomIdAndUserId(roomId, userId)
                .orElseThrow(() -> new RuntimeException("채팅방 멤버가 아닙니다."));
        
        if (member.getRole() != ChatRoomMember.MemberRole.OWNER) {
            throw new RuntimeException("채팅방 소유자만 삭제할 수 있습니다.");
        }
        
        chatRoomRepository.deleteById(roomId);
    }

    @Override
    public boolean isUserMemberOfRoom(Long roomId, Long userId) {
        return chatRoomRepository.isUserMemberOfRoom(roomId, userId);
    }
} 
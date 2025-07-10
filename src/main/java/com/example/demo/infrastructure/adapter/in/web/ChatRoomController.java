package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.dto.ChatRoomDto;
import com.example.demo.application.dto.CreateChatRoomRequest;
import com.example.demo.application.dto.JoinChatRoomRequest;
import com.example.demo.application.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@CrossOrigin(origins = "*")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PostMapping
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        // 임시로 사용자 ID를 1로 설정 (실제로는 인증에서 가져와야 함)
        Long userId = 1L;
        ChatRoomDto chatRoom = chatRoomService.createChatRoom(request, userId);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomDto> getChatRoom(@PathVariable Long roomId) {
        ChatRoomDto chatRoom = chatRoomService.getChatRoom(roomId);
        return ResponseEntity.ok(chatRoom);
    }

    @GetMapping
    public ResponseEntity<List<ChatRoomDto>> getPublicChatRooms() {
        List<ChatRoomDto> chatRooms = chatRoomService.getPublicChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/ott/{ottType}")
    public ResponseEntity<List<ChatRoomDto>> getChatRoomsByOttType(@PathVariable String ottType) {
        List<ChatRoomDto> chatRooms = chatRoomService.getChatRoomsByOttType(ottType);
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatRoomDto>> getUserChatRooms(@PathVariable Long userId) {
        List<ChatRoomDto> chatRooms = chatRoomService.getUserChatRooms(userId);
        return ResponseEntity.ok(chatRooms);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<Void> joinChatRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        JoinChatRoomRequest request = new JoinChatRoomRequest();
        request.setRoomId(roomId);
        
        chatRoomService.joinChatRoom(request, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveChatRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatRoomService.leaveChatRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteChatRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatRoomService.deleteChatRoom(roomId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{roomId}/is-member")
    public ResponseEntity<Boolean> isUserMemberOfRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        boolean isMember = chatRoomService.isUserMemberOfRoom(roomId, userId);
        return ResponseEntity.ok(isMember);
    }
} 
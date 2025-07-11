package com.example.demo.adapter.in.web;

import com.example.demo.application.dto.ChatRoomDto;
import com.example.demo.application.dto.CommonResponse;
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
    public ResponseEntity<CommonResponse<ChatRoomDto>> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        // 임시로 사용자 ID를 1로 설정 (실제로는 인증에서 가져와야 함)
        Long userId = 1L;
        ChatRoomDto chatRoom = chatRoomService.createChatRoom(request, userId);
        return ResponseEntity.ok(CommonResponse.success(chatRoom, "채팅방이 성공적으로 생성되었습니다."));
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<CommonResponse<ChatRoomDto>> getChatRoom(@PathVariable Long roomId) {
        ChatRoomDto chatRoom = chatRoomService.getChatRoom(roomId);
        return ResponseEntity.ok(CommonResponse.success(chatRoom, "채팅방 정보를 성공적으로 조회했습니다."));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ChatRoomDto>>> getPublicChatRooms() {
        List<ChatRoomDto> chatRooms = chatRoomService.getPublicChatRooms();
        return ResponseEntity.ok(CommonResponse.success(chatRooms, "공개 채팅방 목록을 성공적으로 조회했습니다."));
    }

    @GetMapping("/ott/{ottType}")
    public ResponseEntity<CommonResponse<List<ChatRoomDto>>> getChatRoomsByOttType(@PathVariable String ottType) {
        List<ChatRoomDto> chatRooms = chatRoomService.getChatRoomsByOttType(ottType);
        return ResponseEntity.ok(CommonResponse.success(chatRooms, ottType + " 채팅방 목록을 성공적으로 조회했습니다."));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommonResponse<List<ChatRoomDto>>> getUserChatRooms(@PathVariable Long userId) {
        List<ChatRoomDto> chatRooms = chatRoomService.getUserChatRooms(userId);
        return ResponseEntity.ok(CommonResponse.success(chatRooms, "사용자의 채팅방 목록을 성공적으로 조회했습니다."));
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<CommonResponse<Void>> joinChatRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        JoinChatRoomRequest request = new JoinChatRoomRequest();
        request.setRoomId(roomId);
        
        chatRoomService.joinChatRoom(request, userId);
        return ResponseEntity.ok(CommonResponse.success("채팅방에 성공적으로 참여했습니다."));
    }

    @PostMapping("/{roomId}/leave")
    public ResponseEntity<CommonResponse<Void>> leaveChatRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatRoomService.leaveChatRoom(roomId, userId);
        return ResponseEntity.ok(CommonResponse.success("채팅방에서 성공적으로 나갔습니다."));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<CommonResponse<Void>> deleteChatRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        chatRoomService.deleteChatRoom(roomId, userId);
        return ResponseEntity.ok(CommonResponse.success("채팅방이 성공적으로 삭제되었습니다."));
    }

    @GetMapping("/{roomId}/is-member")
    public ResponseEntity<CommonResponse<Boolean>> isUserMemberOfRoom(@PathVariable Long roomId) {
        // 임시로 사용자 ID를 1로 설정
        Long userId = 1L;
        
        boolean isMember = chatRoomService.isUserMemberOfRoom(roomId, userId);
        return ResponseEntity.ok(CommonResponse.success(isMember, "멤버 여부를 성공적으로 확인했습니다."));
    }
} 
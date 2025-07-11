package com.example.demo.adapter.in.web;

import com.example.demo.application.dto.ChatRoomDto;
import com.example.demo.application.dto.CommonResponse;
import com.example.demo.application.dto.CreateChatRoomRequest;
import com.example.demo.application.dto.JoinChatRoomRequest;
import com.example.demo.application.service.ChatRoomService;
import com.example.demo.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatrooms")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<ChatRoomDto>> createChatRoom(
            @RequestBody CreateChatRoomRequest request,
            @AuthenticationPrincipal User user) {
        ChatRoomDto chatRoom = chatRoomService.createChatRoom(request, user.getId());
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
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<Void>> joinChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal User user) {
        
        JoinChatRoomRequest request = new JoinChatRoomRequest();
        request.setRoomId(roomId);
        
        chatRoomService.joinChatRoom(request, user.getId());
        return ResponseEntity.ok(CommonResponse.success("채팅방에 성공적으로 참여했습니다."));
    }

    @PostMapping("/{roomId}/leave")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<Void>> leaveChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal User user) {
        
        chatRoomService.leaveChatRoom(roomId, user.getId());
        return ResponseEntity.ok(CommonResponse.success("채팅방에서 성공적으로 나갔습니다."));
    }

    @DeleteMapping("/{roomId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<Void>> deleteChatRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal User user) {
        
        chatRoomService.deleteChatRoom(roomId, user.getId());
        return ResponseEntity.ok(CommonResponse.success("채팅방이 성공적으로 삭제되었습니다."));
    }

    @GetMapping("/{roomId}/is-member")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommonResponse<Boolean>> isUserMemberOfRoom(
            @PathVariable Long roomId,
            @AuthenticationPrincipal User user) {
        
        boolean isMember = chatRoomService.isUserMemberOfRoom(roomId, user.getId());
        return ResponseEntity.ok(CommonResponse.success(isMember, "멤버 여부를 성공적으로 확인했습니다."));
    }
} 
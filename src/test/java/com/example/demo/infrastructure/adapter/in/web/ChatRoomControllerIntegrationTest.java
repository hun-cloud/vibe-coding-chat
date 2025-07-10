package com.example.demo.infrastructure.adapter.in.web;

import com.example.demo.application.dto.CreateChatRoomRequest;
import com.example.demo.application.service.ChatRoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatRoomController.class)
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"
})
class ChatRoomControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatRoomService chatRoomService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createChatRoom_성공() throws Exception {
        // given
        CreateChatRoomRequest request = new CreateChatRoomRequest();
        request.setName("테스트 채팅방");
        request.setDescription("테스트용 채팅방");
        request.setRoomType("GENERAL");
        request.setIsPrivate(false);

        when(chatRoomService.createChatRoom(any(CreateChatRoomRequest.class), eq(1L)))
                .thenReturn(createMockChatRoomDto());

        // when & then
        mockMvc.perform(post("/api/chatrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 채팅방"))
                .andExpect(jsonPath("$.roomType").value("GENERAL"));
    }

    @Test
    void getChatRoom_성공() throws Exception {
        // given
        when(chatRoomService.getChatRoom(1L))
                .thenReturn(createMockChatRoomDto());

        // when & then
        mockMvc.perform(get("/api/chatrooms/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("테스트 채팅방"));
    }

    @Test
    void getPublicChatRooms_성공() throws Exception {
        // given
        when(chatRoomService.getPublicChatRooms())
                .thenReturn(java.util.Arrays.asList(createMockChatRoomDto()));

        // when & then
        mockMvc.perform(get("/api/chatrooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("테스트 채팅방"));
    }

    @Test
    void joinChatRoom_성공() throws Exception {
        // when & then
        mockMvc.perform(post("/api/chatrooms/1/join"))
                .andExpect(status().isOk());
    }

    @Test
    void leaveChatRoom_성공() throws Exception {
        // when & then
        mockMvc.perform(post("/api/chatrooms/1/leave"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteChatRoom_성공() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/chatrooms/1"))
                .andExpect(status().isOk());
    }

    @Test
    void isUserMemberOfRoom_성공() throws Exception {
        // given
        when(chatRoomService.isUserMemberOfRoom(1L, 1L))
                .thenReturn(true);

        // when & then
        mockMvc.perform(get("/api/chatrooms/1/is-member"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    private com.example.demo.application.dto.ChatRoomDto createMockChatRoomDto() {
        com.example.demo.application.dto.ChatRoomDto dto = new com.example.demo.application.dto.ChatRoomDto();
        dto.setId(1L);
        dto.setName("테스트 채팅방");
        dto.setDescription("테스트용 채팅방");
        dto.setRoomType("GENERAL");
        dto.setIsPrivate(false);
        dto.setIsActive(true);
        dto.setCreatedBy(1L);
        dto.setCreatedByUsername("admin");
        return dto;
    }
} 
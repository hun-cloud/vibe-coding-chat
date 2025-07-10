package com.example.demo.infrastructure.adapter.in.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration,org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration"
})
class WebSocketIntegrationTest {

    @Autowired
    private org.springframework.boot.test.web.server.LocalServerPort serverPort;

    @Test
    void webSocketConnection_성공() throws ExecutionException, InterruptedException, TimeoutException {
        // given
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        CompletableFuture<String> messageFuture = new CompletableFuture<>();

        StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/topic/chat/general", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        messageFuture.complete((String) payload);
                    }
                });

                // 테스트 메시지 전송
                session.send("/app/chat/general", "{\"content\":\"테스트 메시지\",\"messageType\":\"TEXT\"}");
            }
        };

        // when
        StompSession session = stompClient.connect(
                "ws://localhost:" + serverPort + "/ws",
                sessionHandler).get(5, SECONDS);

        // then
        String receivedMessage = messageFuture.get(5, SECONDS);
        assertThat(receivedMessage).contains("테스트 메시지");
        assertThat(session.isConnected()).isTrue();
    }

    @Test
    void webSocketJoinRoom_성공() throws ExecutionException, InterruptedException, TimeoutException {
        // given
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));

        CompletableFuture<String> joinMessageFuture = new CompletableFuture<>();

        StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                session.subscribe("/topic/chat/general", new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders headers) {
                        return String.class;
                    }

                    @Override
                    public void handleFrame(StompHeaders headers, Object payload) {
                        String message = (String) payload;
                        if (message.contains("입장")) {
                            joinMessageFuture.complete(message);
                        }
                    }
                });

                // 채팅방 입장
                session.send("/app/join/general", "{}");
            }
        };

        // when
        StompSession session = stompClient.connect(
                "ws://localhost:" + serverPort + "/ws",
                sessionHandler).get(5, SECONDS);

        // then
        String joinMessage = joinMessageFuture.get(5, SECONDS);
        assertThat(joinMessage).contains("입장");
        assertThat(session.isConnected()).isTrue();
    }
} 
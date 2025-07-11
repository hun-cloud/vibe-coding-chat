# Chat Application Demo

A real-time chat application built with Spring Boot, WebSocket, and MongoDB.

## Architecture

This project follows **Hexagonal Architecture (Ports and Adapters)** principles:

```
com.example.demo/
├── domain/                    # Domain Layer (Pure Business Logic)
│   ├── entity/               # Domain Entities (No JPA/MongoDB annotations)
│   │   ├── ChatRoom.java
│   │   ├── ChatMessage.java
│   │   ├── User.java
│   │   └── ChatRoomMember.java
│   └── repository/           # Repository Interfaces (Ports)
│       ├── ChatRoomRepository.java
│       ├── ChatMessageRepository.java
│       └── UserRepository.java
├── application/              # Application Layer (Use Cases)
│   ├── port/
│   │   ├── in/              # Inbound Ports (Use Case Interfaces)
│   │   └── out/             # Outbound Ports (Repository Interfaces)
│   ├── service/             # Use Case Implementations
│   └── dto/                 # Data Transfer Objects
│       ├── CommonResponse.java  # Standardized API Response
│       ├── ChatRoomDto.java
│       ├── ChatMessageDto.java
│       └── ...
├── infrastructure/           # Infrastructure Layer (Technical Concerns)
│   ├── persistence/         # Data Access Layer
│   │   ├── entity/          # JPA Entities
│   │   ├── document/        # MongoDB Documents
│   │   └── repository/      # Repository Implementations
│   └── config/              # Configuration
└── adapter/                 # Adapter Layer (External Interfaces)
    ├── in/                  # Inbound Adapters (Controllers, WebSocket)
    │   └── web/
    │       ├── ChatRoomController.java
    │       ├── ChatMessageController.java
    │       ├── ChatWebSocketController.java
    │       └── GlobalExceptionHandler.java
    └── out/                 # Outbound Adapters (Repository Adapters)
        └── persistence/
```

## Key Features

- **Pure Domain Layer**: Business logic is completely separated from technical concerns
- **Port and Adapter Pattern**: Clear separation between application and external systems
- **Dependency Inversion**: Domain depends on abstractions, not concrete implementations
- **Technology Independence**: Domain entities have no JPA or MongoDB dependencies
- **Standardized API Responses**: All API endpoints return consistent `CommonResponse<T>` format
- **Global Exception Handling**: Centralized error handling with consistent error responses

## API Response Format

All API endpoints return responses in the following standardized format:

### Success Response
```json
{
  "success": true,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    // Actual response data
  },
  "timestamp": "2024-01-01T12:00:00",
  "errorCode": null
}
```

### Error Response
```json
{
  "success": false,
  "message": "에러 메시지",
  "data": null,
  "timestamp": "2024-01-01T12:00:00",
  "errorCode": "ERROR_CODE"
}
```

## Benefits of This Architecture

1. **Testability**: Domain logic can be tested without database dependencies
2. **Flexibility**: Easy to switch between different persistence technologies
3. **Maintainability**: Clear separation of concerns makes code easier to understand
4. **Scalability**: Each layer can be scaled independently
5. **Consistency**: Standardized API responses improve frontend integration
6. **Error Handling**: Centralized exception handling provides consistent error responses

## Technology Stack

- **Backend**: Spring Boot 3.x
- **Database**: MySQL (JPA) + MongoDB
- **Real-time Communication**: WebSocket (STOMP)
- **Build Tool**: Gradle
- **Lombok**: Reduces boilerplate code

## Getting Started

1. Clone the repository
2. Configure database connections in `application.yml`
3. Run the application: `./gradlew bootRun`
4. Access the chat interface at `http://localhost:8080`

## API Endpoints

### Chat Room Management
- `POST /api/chatrooms` - Create a new chat room
- `GET /api/chatrooms` - Get all public chat rooms
- `GET /api/chatrooms/{roomId}` - Get specific chat room
- `GET /api/chatrooms/ott/{ottType}` - Get chat rooms by OTT type
- `GET /api/chatrooms/user/{userId}` - Get user's chat rooms
- `POST /api/chatrooms/{roomId}/join` - Join a chat room
- `POST /api/chatrooms/{roomId}/leave` - Leave a chat room
- `DELETE /api/chatrooms/{roomId}` - Delete a chat room
- `GET /api/chatrooms/{roomId}/is-member` - Check if user is member

### Message Management
- `GET /api/chatrooms/{roomId}/messages` - Get chat messages
- `GET /api/chatrooms/{roomId}/messages/date/{date}` - Get messages by date
- `DELETE /api/chatrooms/{roomId}/messages/{messageId}` - Delete a message
- `POST /api/chatrooms/{roomId}/messages/mark-read` - Mark messages as read
- `GET /api/chatrooms/{roomId}/messages/unread-count` - Get unread message count

## WebSocket Endpoints

- `/app/chat/{roomId}` - Send a message to a room
- `/app/join/{roomId}` - Join a room via WebSocket
- `/app/leave/{roomId}` - Leave a room via WebSocket
- `/topic/chat/{roomId}` - Subscribe to room messages 
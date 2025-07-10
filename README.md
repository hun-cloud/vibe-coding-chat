# 🎬 OTT 채팅 서비스

헥사고날 아키텍처를 기반으로 한 OTT(Over-The-Top) 플랫폼별 채팅 서비스입니다.

## 🏗️ 아키텍처

### 헥사고날 아키텍처 (Hexagonal Architecture)
- **Domain Layer**: 핵심 비즈니스 로직과 엔티티
- **Application Layer**: 유스케이스와 포트 인터페이스
- **Infrastructure Layer**: 외부 시스템과의 통신 (DB, Redis, WebSocket)

### 기술 스택
- **Backend**: Spring Boot 3.5.3, Java 17
- **Database**: 
  - H2 (개발용), JPA
  - MongoDB (채팅 메시지 저장, 30일 TTL)
- **Cache & Pub/Sub**: Redis
- **WebSocket**: STOMP over WebSocket
- **Frontend**: HTML5, JavaScript, SockJS, Stomp.js

## 🚀 주요 기능

### 채팅방 관리
- ✅ OTT별 채팅방 (Netflix, Disney+, Wavve, Tving, Watcha 등)
- ✅ 일반 채팅방
- ✅ 그룹 채팅방
- ✅ 비공개 채팅방

### 실시간 채팅
- ✅ WebSocket을 통한 실시간 메시지 전송
- ✅ Redis Pub/Sub을 통한 메시지 브로드캐스팅
- ✅ 채팅방별 메시지 분리

### 메시지 관리
- ✅ MongoDB에 메시지 저장 (30일 자동 삭제)
- ✅ 메시지 타입 지원 (TEXT, IMAGE, FILE, SYSTEM, EMOJI)
- ✅ 읽지 않은 메시지 카운트

### 사용자 관리
- ✅ 사용자 등록/로그인
- ✅ 채팅방 멤버 관리
- ✅ 권한 관리 (OWNER, ADMIN, MODERATOR, MEMBER)

## 📁 프로젝트 구조

```
src/main/java/com/example/demo/
├── domain/                    # 도메인 레이어
│   ├── entity/               # JPA 엔티티
│   │   ├── User.java
│   │   ├── ChatRoom.java
│   │   └── ChatRoomMember.java
│   └── document/             # MongoDB 도큐먼트
│       └── ChatMessage.java
├── application/              # 애플리케이션 레이어
│   ├── port/                # 포트 인터페이스
│   │   ├── in/              # 인바운드 포트
│   │   │   ├── ChatRoomUseCase.java
│   │   │   └── ChatMessageUseCase.java
│   │   └── out/             # 아웃바운드 포트
│   │       ├── ChatRoomRepository.java
│   │       ├── ChatMessageRepository.java
│   │       └── ChatMessagePublisher.java
│   └── dto/                 # 데이터 전송 객체
│       ├── ChatRoomDto.java
│       ├── ChatMessageDto.java
│       └── ...
└── infrastructure/          # 인프라스트럭처 레이어
    ├── config/              # 설정 클래스
    │   ├── WebSocketConfig.java
    │   └── RedisConfig.java
    ├── adapter/             # 어댑터 구현체
    └── controller/          # REST API 컨트롤러
```

## 🛠️ 설치 및 실행

### 필수 요구사항
- Java 17+
- Gradle 8.5+
- Redis (선택사항 - 개발 시)
- MongoDB (선택사항 - 개발 시)

### 1. 프로젝트 클론
```bash
git clone <repository-url>
cd demo
```

### 2. 애플리케이션 실행
```bash
# 개발 모드 (H2 DB 사용)
./gradlew bootRun

# 또는 빌드 후 실행
./gradlew build
java -jar build/libs/demo-0.0.1-SNAPSHOT.jar
```

### 3. 접속
- **웹 애플리케이션**: http://localhost:8080
- **H2 콘솔**: http://localhost:8080/h2-console

## 🔧 설정

### application.yml 주요 설정
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb  # 개발용 H2 DB
  
  data:
    mongodb:
      host: localhost
      port: 27017
      database: ott_chat_db
  
  redis:
    host: localhost
    port: 6379

chat:
  message:
    retention-days: 30      # 메시지 보관 기간
    max-length: 1000        # 최대 메시지 길이
  room:
    max-participants: 100   # 최대 참여자 수
```

## 📡 API 엔드포인트

### WebSocket
- **연결**: `/ws` (SockJS)
- **메시지 전송**: `/app/chat/{roomId}`
- **메시지 수신**: `/topic/chat/{roomId}`

### REST API (구현 예정)
- `GET /api/chatrooms` - 채팅방 목록
- `POST /api/chatrooms` - 채팅방 생성
- `GET /api/chatrooms/{id}/messages` - 메시지 목록
- `POST /api/chatrooms/{id}/join` - 채팅방 참여

## 🎯 개발 로드맵

### Phase 1: 기본 채팅 기능 ✅
- [x] 헥사고날 아키텍처 설계
- [x] 도메인 모델 구현
- [x] WebSocket 설정
- [x] 기본 UI 구현

### Phase 2: 고급 기능 (진행 중)
- [ ] 사용자 인증/인가
- [ ] 채팅방 관리 API
- [ ] 메시지 저장/조회
- [ ] Redis Pub/Sub 구현

### Phase 3: 확장 기능
- [ ] 파일 업로드
- [ ] 이모지 지원
- [ ] 알림 기능
- [ ] 모바일 앱 연동

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해주세요. 
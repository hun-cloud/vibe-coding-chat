package com.example.demo.infrastructure.adapter.out.persistence;

import com.example.demo.application.port.out.ChatMessageRepository;
import com.example.demo.domain.document.ChatMessage;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MongoChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageRepository {

    @Override
    Optional<ChatMessage> findById(String id);

    @Override
    @Query(value = "{'roomId': ?0, 'isDeleted': false}", sort = "{'createdAt': -1}")
    List<ChatMessage> findByRoomIdOrderByCreatedAtDesc(Long roomId, int page, int size);

    @Override
    @Query("{'roomId': ?0, 'createdAt': {$gte: ?1, $lte: ?2}, 'isDeleted': false}")
    List<ChatMessage> findByRoomIdAndCreatedAtBetween(Long roomId, LocalDateTime start, LocalDateTime end);

    @Override
    @Query(value = "{'roomId': ?0, 'isDeleted': false}", count = true)
    long countByRoomId(Long roomId);

    @Override
    @Query("{'roomId': ?0, 'createdAt': {$gt: ?1}, 'isDeleted': false}")
    List<ChatMessage> findUnreadMessages(Long roomId, LocalDateTime lastReadAt);

    @Override
    @Query(value = "{'createdAt': {$lt: ?0}}", delete = true)
    void deleteOldMessages(LocalDateTime before);
} 
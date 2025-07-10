package com.example.demo.infrastructure.adapter.out.persistence;

import com.example.demo.application.port.out.ChatRoomRepository;
import com.example.demo.domain.entity.ChatRoom;
import com.example.demo.domain.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepository {

    @Override
    @Query("SELECT c FROM ChatRoom c WHERE c.ottType = :ottType AND c.isActive = true")
    List<ChatRoom> findByOttType(@Param("ottType") String ottType);

    @Override
    @Query("SELECT c FROM ChatRoom c WHERE c.isPrivate = false AND c.isActive = true")
    List<ChatRoom> findPublicRooms();

    @Override
    @Query("SELECT DISTINCT c FROM ChatRoom c " +
           "JOIN ChatRoomMember m ON c.id = m.chatRoom.id " +
           "WHERE m.user.id = :userId AND m.isActive = true AND c.isActive = true")
    List<ChatRoom> findByUserId(@Param("userId") Long userId);

    @Override
    @Query("SELECT m FROM ChatRoomMember m WHERE m.chatRoom.id = :roomId AND m.user.id = :userId AND m.isActive = true")
    Optional<ChatRoomMember> findMemberByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Override
    @Query("SELECT m FROM ChatRoomMember m WHERE m.chatRoom.id = :roomId AND m.isActive = true")
    List<ChatRoomMember> findMembersByRoomId(@Param("roomId") Long roomId);

    @Override
    @Query("DELETE FROM ChatRoomMember m WHERE m.chatRoom.id = :roomId AND m.user.id = :userId")
    void deleteMemberByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Override
    @Query("SELECT COUNT(m) > 0 FROM ChatRoomMember m WHERE m.chatRoom.id = :roomId AND m.user.id = :userId AND m.isActive = true")
    boolean isUserMemberOfRoom(@Param("roomId") Long roomId, @Param("userId") Long userId);
} 
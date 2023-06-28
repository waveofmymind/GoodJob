package com.goodjob.core.domain.chat.repository;

import com.goodjob.core.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT cm FROM ChatMessage cm JOIN FETCH cm.chatRoom cr JOIN FETCH cr.sender JOIN FETCH cr.receiver WHERE cr.roomId = :roomId")
    List<ChatMessage> findByChatRoom_RoomId(@Param("roomId") String roomId);

}

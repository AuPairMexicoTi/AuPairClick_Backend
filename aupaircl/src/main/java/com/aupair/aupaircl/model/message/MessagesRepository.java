package com.aupair.aupaircl.model.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessagesRepository extends JpaRepository<Messages, UUID> {
    List<Messages> findAllByConversation_ConversationId(UUID conversationId);
    List<Messages> findAllByConversation_ConversationIdOrderByTimestampAsc(UUID conversationId);

    @Query("SELECT m FROM Messages m WHERE m.conversation.conversationId = :conversationId ORDER BY m.timestamp DESC")
    List<Messages> findTopByConversationOrderByTimestampDesc(@Param("conversationId") UUID conversationId);
}

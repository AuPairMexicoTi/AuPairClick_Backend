package com.aupair.aupaircl.model.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessagesRepository extends JpaRepository<Messages, UUID> {
    List<Messages> findByConversation_ConversationId(UUID conversationId);

    @Query("SELECT m FROM Messages m WHERE m.conversation.conversationId = :conversationId ORDER BY m.timestamp DESC")
    Optional<Messages> findTopByConversationOrderByTimestampDesc(@Param("conversationId") UUID conversationId);
}

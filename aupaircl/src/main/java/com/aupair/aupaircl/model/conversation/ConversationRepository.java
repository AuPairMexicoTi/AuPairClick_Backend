package com.aupair.aupaircl.model.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {
    Optional<Conversation> findByUser1_EmailAndAndUser2_Email(String email1,String email2);

    @Query("SELECT c FROM Conversation c WHERE c.user1.email = :userId OR c.user2.email = :userId")
    List<Conversation> findByUser(@Param("userId") String userId);
}

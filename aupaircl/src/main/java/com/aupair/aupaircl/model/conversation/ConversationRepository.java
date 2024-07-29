package com.aupair.aupaircl.model.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversation, UUID> {

    Optional<Conversation> findBySender_EmailAndAndReceiver_Email(String email1,String email2);

    @Query("SELECT c FROM Conversation c WHERE (c.sender.email = :senderEmail AND c.receiver.email = :receiverEmail) OR (c.sender.email = :receiverEmail AND c.receiver.email = :senderEmail)")
    Optional<Conversation> findBySenderAndReceiver(@Param("senderEmail") String senderEmail, @Param("receiverEmail") String receiverEmail);

    @Query("SELECT c FROM Conversation c WHERE c.sender.email = :userId OR c.receiver.email = :userId")
    List<Conversation> findAllByUser(@Param("userId") String userId);
}

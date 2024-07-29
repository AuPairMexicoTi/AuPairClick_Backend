package com.aupair.aupaircl.model.conversation;

import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_conversations")
public class Conversation {

    @Id
    @Column(name = "id_conversation", nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID conversationId;

    @ManyToOne
    @JoinColumn(name = "fk_sender", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "fk_receiver", nullable = false)
    private User receiver;
    @PrePersist
    private void generateUUID(){

        if(this.conversationId==null){
            this.conversationId = UUID.randomUUID();
        }
    }
}

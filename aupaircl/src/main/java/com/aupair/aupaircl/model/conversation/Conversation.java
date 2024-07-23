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
    @JoinColumn(name = "fk_user_1", nullable = false)
    private User user1;

    @ManyToOne
    @JoinColumn(name = "fk_user_2", nullable = false)
    private User user2;
    @PrePersist
    private void generateUUID(){

        if(this.conversationId==null){
            this.conversationId = UUID.randomUUID();
        }
    }
}

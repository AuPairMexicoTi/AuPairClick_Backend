package com.aupair.aupaircl.model.message;

import com.aupair.aupaircl.model.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "apc_messages")
public class Messages {

    @Id
    @Column(name = "id_messages",nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID messageId;

    @ManyToOne
    @JoinColumn(name = "fk_sender", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "fk_receiver", nullable = false)
    private User receiver;

    @Column(name = "contenct",nullable = false)
    private String content;

    private Date sentAt;
    @PrePersist
    private void generateUUID(){
        if(this.sentAt==null){
            this.sentAt= new Date();
        }
        if(this.messageId==null){
            this.messageId = UUID.randomUUID();
        }
    }
}


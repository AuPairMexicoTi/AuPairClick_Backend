package com.aupair.aupaircl.model.message;

import com.aupair.aupaircl.model.conversation.Conversation;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

import java.time.LocalDateTime;
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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "fk_conversation", nullable = false)
    private Conversation conversation;


    @Column(name = "content",nullable = false)
    private String content;
    @Column(name = "lastMessage",nullable = false)
    private String lastMessage;
    @Column(name = "sendDate ",nullable = false)
    private LocalDateTime timestamp;
    @Column(name = "send_by_sender",nullable = false)
    private boolean sentBySender;
    @Column(name = "send_by_type",nullable = false)
    private String sentByType;
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


package com.aupair.aupaircl.controller.messagescontroller.messagesdto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResponseConversationDto {
    private String conversationId;
    private String receiver;
    private String receiverEmail;
    private String receiverGender;
    private String receiverAge;
    private String receiverNationality;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date receiverLastSeen;
    private String content;
    private String lastMessage;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastDate;
    private String imageAvatarSender;
    private String imageAvatarReceiver;
}

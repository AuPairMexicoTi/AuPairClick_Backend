package com.aupair.aupaircl.controller.messagescontroller.messagesdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ResponseConversationDto {
    private String conversationId;
    private String receiver;
    private String content;
    private String lastMessage;
    private LocalDateTime lastDate;
    private String imageAvatar;
}

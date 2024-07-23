package com.aupair.aupaircl.controller.messagescontroller.messagesdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MessageDto {
    private String senderId;
    private String receiverId;
    private String content;
}

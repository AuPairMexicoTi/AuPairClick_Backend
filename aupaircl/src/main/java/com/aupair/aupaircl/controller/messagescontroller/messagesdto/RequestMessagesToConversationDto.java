package com.aupair.aupaircl.controller.messagescontroller.messagesdto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class RequestMessagesToConversationDto {
private String currentUser;
private UUID conversationId;
}

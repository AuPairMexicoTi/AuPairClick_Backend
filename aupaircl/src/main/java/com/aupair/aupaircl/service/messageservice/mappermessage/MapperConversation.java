package com.aupair.aupaircl.service.messageservice.mappermessage;
import com.aupair.aupaircl.controller.messagescontroller.messagesdto.ResponseConversationDto;
import com.aupair.aupaircl.model.conversation.Conversation;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import com.aupair.aupaircl.model.message.Messages;
import com.aupair.aupaircl.model.message.MessagesRepository;
import com.aupair.aupaircl.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MapperConversation {

    private final MessagesRepository messageRepository;
    private final ImageRepository imageRepository;
    @Autowired
    public MapperConversation(MessagesRepository messageRepository, ImageRepository imageRepository) {
        this.messageRepository = messageRepository;
        this.imageRepository = imageRepository;
    }

    public ResponseConversationDto mapToDto(Conversation conversation, User user) {
        Messages lastMessage = messageRepository.findTopByConversationOrderByTimestampDesc(conversation.getConversationId())
                .orElse(null);

        ResponseConversationDto dto = new ResponseConversationDto();
        dto.setConversationId(conversation.getConversationId().toString());
        dto.setReceiver(conversation.getUser1().equals(user) ? conversation.getUser2().getUsername() : conversation.getUser1().getUsername());
        dto.setLastMessage(lastMessage != null ? lastMessage.getContent() : "No messages yet");
        dto.setLastDate(lastMessage != null ? lastMessage.getTimestamp() : null);
        List<Image> images = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(user.getEmail(),true);
        dto.setImageAvatar(images.get(0).getImageName());
        return dto;
    }

    public List<ResponseConversationDto> mapToDtos(List<Conversation> conversations, User user) {
        return conversations.stream()
                .map(conversation -> mapToDto(conversation, user))
                .collect(Collectors.toList());
    }
}

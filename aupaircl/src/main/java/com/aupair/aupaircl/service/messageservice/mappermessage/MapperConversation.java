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

import java.time.LocalDateTime;
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
        List<Messages> messagesList = messageRepository.findTopByConversationOrderByTimestampDesc(conversation.getConversationId());
        Messages lastMessage = messagesList.isEmpty() ? null : messagesList.get(0);

        ResponseConversationDto dto = new ResponseConversationDto();
        dto.setConversationId(conversation.getConversationId().toString());
        dto.setReceiverEmail(conversation.getSender().equals(user) ? conversation.getReceiver().getEmail() : conversation.getSender().getEmail());
        dto.setReceiver(conversation.getSender().equals(user) ? conversation.getReceiver().getUsername() : conversation.getSender().getUsername());
        dto.setLastMessage(lastMessage != null ? lastMessage.getContent() : "Sin mensajes aun");
        dto.setLastDate(lastMessage != null ? lastMessage.getTimestamp() : LocalDateTime.now());

        // Imágenes del remitente
        List<Image> imagesSender = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(user.getEmail(), true);
        dto.setImageAvatarSender(!imagesSender.isEmpty() ? imagesSender.get(0).getImageName() : "https://cdn2.iconfinder.com/data/icons/data-privacy-and-gdpr/512/GDPR3-18-512.png");

        // Imágenes del receptor
        String receiverEmail = conversation.getSender().equals(user) ? conversation.getReceiver().getEmail() : conversation.getSender().getEmail();
        List<Image> imagesReceiver = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(receiverEmail, true);
        dto.setImageAvatarReceiver(!imagesReceiver.isEmpty() ? imagesReceiver.get(0).getImageName() : "https://cdn2.iconfinder.com/data/icons/data-privacy-and-gdpr/512/GDPR3-18-512.png");

        return dto;
    }

    public List<ResponseConversationDto> mapToDtos(List<Conversation> conversations, User user) {
        return conversations.stream()
                .map(conversation -> mapToDto(conversation, user))
                .collect(Collectors.toList());
    }
}

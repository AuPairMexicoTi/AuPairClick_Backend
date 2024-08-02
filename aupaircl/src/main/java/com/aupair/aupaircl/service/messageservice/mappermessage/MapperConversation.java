package com.aupair.aupaircl.service.messageservice.mappermessage;

import com.aupair.aupaircl.controller.messagescontroller.messagesdto.ResponseConversationDto;
import com.aupair.aupaircl.model.conversation.Conversation;
import com.aupair.aupaircl.model.image.Image;
import com.aupair.aupaircl.model.image.ImageRepository;
import com.aupair.aupaircl.model.message.Messages;
import com.aupair.aupaircl.model.message.MessagesRepository;
import com.aupair.aupaircl.model.profile.Profile;
import com.aupair.aupaircl.model.profile.ProfileRepository;
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
    private final ProfileRepository profileRepository;

    @Autowired
    public MapperConversation(MessagesRepository messageRepository, ImageRepository imageRepository, ProfileRepository profileRepository) {
        this.messageRepository = messageRepository;
        this.imageRepository = imageRepository;
        this.profileRepository = profileRepository;
    }

    public ResponseConversationDto mapToDto(Conversation conversation, User user) {
        List<Messages> messagesList = messageRepository.findTopByConversationOrderByTimestampDesc(conversation.getConversationId());
        Messages lastMessage = messagesList.isEmpty() ? null : messagesList.get(0);

        ResponseConversationDto dto = new ResponseConversationDto();
        dto.setConversationId(conversation.getConversationId().toString());

        User receiver = conversation.getSender().equals(user) ? conversation.getReceiver() : conversation.getSender();
        User sender = conversation.getSender().equals(user) ? conversation.getSender() : conversation.getReceiver();

        dto.setReceiverEmail(receiver.getEmail());
        dto.setReceiver(receiver.getUsername());
        dto.setLastMessage(lastMessage != null ? lastMessage.getContent() : "Sin mensajes aun");
        dto.setLastDate(lastMessage != null ? lastMessage.getTimestamp() : LocalDateTime.now());

        // Obtener el perfil del receptor si está aprobado
        Profile profileSaved = profileRepository.findByUser_EmailAndIsApproved(receiver.getEmail(), true);

        if (profileSaved != null && profileSaved.getUser().getAuPairProfile() != null) {
            dto.setReceiverGender(profileSaved.getUser().getAuPairProfile().getGender().getGenderName());
        } else {
            dto.setReceiverGender("Familia");
        }

        dto.setReceiverNationality(receiver.getProfile() != null && receiver.getProfile().getCountry() != null
                ? receiver.getProfile().getCountry().getNationality()
                : "N/A");

        dto.setReceiverLastSeen(receiver.getLastLogin() != null
                ? receiver.getLastLogin()
                :LocalDateTime.now());

        // Obtener las imágenes del remitente y del receptor
        List<Image> imagesSender = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(sender.getEmail(), true);
        dto.setImageAvatarSender(!imagesSender.isEmpty() ? imagesSender.get(0).getImageName() : "https://cdn2.iconfinder.com/data/icons/data-privacy-and-gdpr/512/GDPR3-18-512.png");

        List<Image> imagesReceiver = imageRepository.findByProfile_User_EmailAndProfile_IsApproved(receiver.getEmail(), true);
        dto.setImageAvatarReceiver(!imagesReceiver.isEmpty() ? imagesReceiver.get(0).getImageName() : "https://cdn2.iconfinder.com/data/icons/data-privacy-and-gdpr/512/GDPR3-18-512.png");

        return dto;
    }

    public List<ResponseConversationDto> mapToDtos(List<Conversation> conversations, User user) {
        return conversations.stream()
                .map(conversation -> mapToDto(conversation, user))
                .collect(Collectors.toList());
    }
}

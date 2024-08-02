package com.aupair.aupaircl.service.messageservice;

import com.aupair.aupaircl.controller.messagescontroller.messagesdto.MessageDto;
import com.aupair.aupaircl.controller.messagescontroller.messagesdto.RequestMessagesToConversationDto;
import com.aupair.aupaircl.controller.messagescontroller.messagesdto.ResponseConversationDto;
import com.aupair.aupaircl.model.conversation.Conversation;
import com.aupair.aupaircl.model.conversation.ConversationRepository;
import com.aupair.aupaircl.model.message.Messages;
import com.aupair.aupaircl.model.message.MessagesRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.service.messageservice.mappermessage.MapperConversation;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MapperConversation mapperConversation;
    private MailService mailService;
    @Autowired
    public MessageService(MessagesRepository messagesRepository, UserRepository userRepository, ConversationRepository conversationRepository,
    MapperConversation mapperConversation,MailService mailService
    ) {
        this.messagesRepository = messagesRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.mapperConversation = mapperConversation;
        this.mailService = mailService;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> sendMessage(MessageDto messageDto) {
        try {
            Optional<Conversation> conversationExist = this.conversationRepository.findBySenderAndReceiver(messageDto.getSenderId(), messageDto.getReceiverId());
            if (conversationExist.isEmpty()) {
                log.error("No hay una conversacion");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.NOT_FOUND.value(), "No hay una conversación"));
            }

            Optional<User> sender = this.userRepository.findByEmail(messageDto.getSenderId());
            Optional<User> receiver = this.userRepository.findByEmail(messageDto.getReceiverId());
            if (sender.isEmpty() || receiver.isEmpty()) {
                log.error("No se encontraron los usuarios");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Usuario inválido al enviar mensaje"));
            }

            if (sender.get().getRole().equals("family") || receiver.get().getRole().equals("family")) {
                log.error("No se puede enviar un mensaje entre familias");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.FORBIDDEN.value(), "No se puede enviar un mensaje a una familia"));
            }

            if (sender.get().getRole().equals("aupair") && receiver.get().getRole().equals("aupair")) {
                log.error("No se puede enviar un mensaje entre aupairs");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.FORBIDDEN.value(), "No se puede enviar un mensaje a un aupair"));
            }

            Conversation conversation = conversationExist.get();

            User actualSender = conversation.getSender().getEmail().equals(messageDto.getSenderId()) ? conversation.getSender() : conversation.getReceiver();
            User actualReceiver = conversation.getSender().getEmail().equals(messageDto.getReceiverId()) ? conversation.getSender() : conversation.getReceiver();

            Messages message = new Messages();
            message.setConversation(conversation);
            message.setContent(messageDto.getContent());
            message.setTimestamp(LocalDateTime.now());
            message.setLastMessage(messageDto.getContent());
            message.setSender(actualSender);
            message.setReceiver(actualReceiver);
            message.setSentBySender(actualSender.getEmail().equals(messageDto.getSenderId()));
            message.setSentByType(actualSender.getRole().getRoleName());
            messagesRepository.save(message);
            this.mailService.sendNotification(actualReceiver, messageDto.getContent());

            return new ResponseEntity<>(new CustomResponse(false, HttpStatus.OK.value(), "Mensaje enviado"), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Fallo enviar mensaje", e);
            return new ResponseEntity<>(new CustomResponse(false, 500, "Error al enviar mensaje"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getMessagesForConversation(RequestMessagesToConversationDto requestMessagesTo) {
        try {
            List<Messages> messages = messagesRepository.findAllByConversation_ConversationIdOrderByTimestampAsc(requestMessagesTo.getConversationId());

            String currentUserEmail = requestMessagesTo.getCurrentUser();

            messages.forEach(message -> {
                boolean isSentBySender = message.getSender().getEmail().equals(currentUserEmail);
                message.setSentBySender(isSentBySender);
            });

            return new ResponseEntity<>(new CustomResponse("Lista de mensajes", HttpStatus.OK.value(), false, messages), HttpStatus.OK);

        } catch (Exception e) {
            log.error("Fallo obtener los mensajes de la conversacion", e);
            return new ResponseEntity<>(new CustomResponse(false, 500, "Error al obtener mensajes de conversacion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getConversationByUser(String email) {
        try {
            Optional<User> user = this.userRepository.findByEmail(email);
            if (user.isEmpty()) {
                log.error("Usuario no registrado");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.NOT_FOUND.value(), "Usuario no registrado"));
            }
            List<Conversation> conversations = this.conversationRepository.findAllByUser(user.get().getEmail());

            if (conversations.isEmpty()) {
                log.error("No hay conversaciones del usuario");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.NOT_FOUND.value(), "No hay conversaciones"));
            }

            List<ResponseConversationDto> dtoList = mapperConversation.mapToDtos(conversations, user.get());

            return new ResponseEntity<>(new CustomResponse("Conversaciones del usuario", HttpStatus.OK.value(), false, dtoList), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Fallo obtener la conversacion por usuario: " + e.getMessage());
            return new ResponseEntity<>(new CustomResponse(false, 500, "Error al obtener conversacion por usuario"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> createConversation(MessageDto messageDto){
        try {
            Optional<User> sender = this.userRepository.findByEmail(messageDto.getSenderId());
            Optional<User> receiver = this.userRepository.findByProfile_NumPerfil(messageDto.getReceiverId());
            if (sender.isEmpty() ||receiver.isEmpty()){
                log.error("No se encontraron los usuarios");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.BAD_REQUEST.value(), "Usuario invalido al enviar mensaje"));
            }
            if (sender.get().getRole().equals("family") || receiver.get().getRole().equals("family")){
                log.error("No se puede enviar un mensaje entre familias");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false,HttpStatus.FORBIDDEN.value(), "No se puede enviar un mensaje a una familia"));
            }
            if (sender.get().getRole().equals("aupair") && receiver.get().getRole().equals("aupair")){
                log.error("No se puede enviar un mensaje entre aupairs");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.FORBIDDEN.value(), "No se puede enviar un mensaje a un aupair"));
            }
            Optional<Conversation> conversation = conversationRepository.findBySenderAndReceiver(sender.get().getEmail(),receiver.get().getEmail());
                if (conversation.isEmpty()){
                    Conversation newConversation = new Conversation();
                    newConversation.setSender(sender.get());
                    newConversation.setReceiver(receiver.get());
                    conversationRepository.save(newConversation);
                }
                return new ResponseEntity<>(new CustomResponse(false,HttpStatus.OK.value(),"Conversacion creada"), HttpStatus.OK);
        }catch (Exception e){
            log.error("Fallo al crear la conversacion" +e.getMessage());
            return new ResponseEntity<>(new CustomResponse(false, 500, "Error al crear la conversacion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

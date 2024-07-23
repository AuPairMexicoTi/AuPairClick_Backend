package com.aupair.aupaircl.service.messageservice;

import com.aupair.aupaircl.controller.messagescontroller.messagesdto.MessageDto;
import com.aupair.aupaircl.controller.messagescontroller.messagesdto.ResponseConversationDto;
import com.aupair.aupaircl.model.conversation.Conversation;
import com.aupair.aupaircl.model.conversation.ConversationRepository;
import com.aupair.aupaircl.model.message.Messages;
import com.aupair.aupaircl.model.message.MessagesRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
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
import java.util.UUID;

@Service
@Slf4j
public class MessageService {
    private final MessagesRepository messagesRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MapperConversation mapperConversation;
    @Autowired
    public MessageService(MessagesRepository messagesRepository, UserRepository userRepository, ConversationRepository conversationRepository,
    MapperConversation mapperConversation
    ) {
        this.messagesRepository = messagesRepository;
        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.mapperConversation = mapperConversation;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> sendMessage(MessageDto messageDto) {
       try {
            User sender = userRepository.findByEmail(messageDto.getSenderId()).orElseThrow(() -> new RuntimeException("Usuario de origen no encontrado"));
            User receiver = userRepository.findByEmail(messageDto.getReceiverId()).orElseThrow(() -> new RuntimeException("Usuario de destino no encontrado"));

           Conversation conversation = conversationRepository.findByUser1_EmailAndAndUser2_Email(messageDto.getSenderId(), messageDto.getReceiverId())
                   .orElseGet(() -> {
                       Conversation newConversation = new Conversation();
                       newConversation.setUser1(sender);
                       newConversation.setUser2(receiver);
                       return conversationRepository.save(newConversation);
                   });

           Messages message = new Messages();
           message.setConversation(conversation);
           message.setContent(message.getContent());
           message.setTimestamp(LocalDateTime.now());

           messagesRepository.save(message);
           return new ResponseEntity<>(new CustomResponse(false,HttpStatus.OK.value(), "Mensaje enviado"), HttpStatus.OK);
       }catch (Exception e){
            log.error("Fallo enviar mensaje");
            return new ResponseEntity<>(new CustomResponse(false, 500, "Error al enviar mensaje"), HttpStatus.INTERNAL_SERVER_ERROR);

       }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getMessagesForConversation(UUID conversationId) {
        try {
            List<Messages> messages = messagesRepository.findByConversation_ConversationId(conversationId);
            return new ResponseEntity<>(new CustomResponse("Lista de mensajes",HttpStatus.OK.value(), false,messages),HttpStatus.OK);

        }catch (Exception e) {
            log.error("Fallo obtener los mensajes de la conversacion");
            return new ResponseEntity<>(new CustomResponse(false, 500, "Error al obtener mensajes de conversacion"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public ResponseEntity<CustomResponse> getConversationByUser(String email){
        try {
            Optional<User> user = this.userRepository.findByEmail(email);
            if (user.isEmpty()){
                log.error("Usuario no registrado");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.NOT_FOUND.value(), "Usuario no registrado"));
            }
            List<Conversation> conversations = this.conversationRepository.findByUser(user.get().getEmail());

            List<ResponseConversationDto> dtoList = mapperConversation .mapToDtos(conversations,user.get());
            if (conversations.isEmpty()){
                log.error("No hay conversaciones del usuario");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.NOT_FOUND.value(), "No hay conversaciones"));
            }
            return new ResponseEntity<>(new CustomResponse("Conversaciones del usuario",HttpStatus.OK.value(), false, dtoList), HttpStatus.OK);
        }catch (Exception e) {
            log.error("Fallo obtener la conversacion por usuario");
            return new ResponseEntity<>(new CustomResponse(false, 500, "Error al obtener conversacion por usuario"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.aupair.aupaircl.controller.messagescontroller;

import com.aupair.aupaircl.controller.messagescontroller.messagesdto.MessageDto;
import com.aupair.aupaircl.controller.messagescontroller.messagesdto.RequestMessagesToConversationDto;
import com.aupair.aupaircl.model.user.UserEmailDto;
import com.aupair.aupaircl.service.messageservice.MessageService;
import com.aupair.aupaircl.utils.CustomResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = {"*"}, allowedHeaders = {"*"})
public class MessagesController {
    private final MessageService messageService;
    @Autowired
    public MessagesController(MessageService messageService) {
        this.messageService = messageService;
    }
    @PostMapping(value = "/sendMessage", produces = "application/json")
    public ResponseEntity<CustomResponse> sendMessage(@Valid @RequestBody MessageDto messageDto){
        try {
            return this.messageService.sendMessage(messageDto);
        }catch (Exception e){
            return new ResponseEntity<>(new CustomResponse(
                    true, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo sucedio al enviar mensaje"), HttpStatus.OK);        }
    }
    @PostMapping(value ="/getMessagesForConversation",produces = "application/json")
    public ResponseEntity<CustomResponse> getMessagesForConversation(@RequestBody  RequestMessagesToConversationDto requestMessagesTo){
        try {
            return this.messageService.getMessagesForConversation(requestMessagesTo);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal al obtener las preferencias"));
        }
    }
    @PostMapping(value ="/getConversationsByUser",produces = "application/json")
    public ResponseEntity<CustomResponse> getConversationsByUser(@Valid @RequestBody UserEmailDto  userEmailDto){
        try {
            return this.messageService.getConversationByUser(userEmailDto.getEmail());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal al obtener las preferencias"));
        }
    }
    @PostMapping(value="/createConversation")
    public ResponseEntity<CustomResponse> createConversation(@RequestBody MessageDto messageDto){
        try {
        return this.messageService.createConversation(messageDto);
        }catch (Exception e){
            return new ResponseEntity<>(new CustomResponse(
                    true, HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo sucedio al crear la conversación"), HttpStatus.OK);
        }
    }
    private String getAuthenticatedUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}

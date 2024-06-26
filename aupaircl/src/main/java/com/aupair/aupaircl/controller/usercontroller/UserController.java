package com.aupair.aupaircl.controller.usercontroller;

import com.aupair.aupaircl.controller.mailcontroller.maildto.MailDTO;
import com.aupair.aupaircl.controller.usercontroller.userdto.UserDTO;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.service.userservice.UserService;
import com.aupair.aupaircl.utils.CustomResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/userAccount")
@CrossOrigin(origins = {"http://localhost:5173/"})
@Slf4j
public class UserController {
    private final UserService   userService;
    private static final String INTERNALSERVER = "Algo sucedio en el servidor";
    private final MailService mailService;

    @Autowired
    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }
    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<CustomResponse> registerUser( @Valid  @RequestBody UserDTO userDTO){
        try {
            return this.userService.registerUser(userDTO);
        }catch (Exception e){
            log.error(" Failed to register user" +e.getMessage());
            return new ResponseEntity<>(new CustomResponse(
                    true, HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNALSERVER), HttpStatus.OK);        }
    }
    @GetMapping(value ="/preferencesByUser/{email}",produces = "application/json")
    public ResponseEntity<CustomResponse> preferencesByUser(@PathVariable("email") String email){
        try {
            return this.userService.getPreferencesCountryByUser(email);
        }catch (Exception e){
            log.error("Error"+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal"));
        }

    }
    @GetMapping(value ="/preferencesByFamily/{email}",produces = "application/json")
    public ResponseEntity<CustomResponse> preferencesByFamily(@PathVariable("email") String email){
        try {
            return this.userService.getPreferencesCountryByFamily(email);
        }catch (Exception e){
            log.error("Error"+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal"));
        }
    }
    @PostMapping(value = "/updatePassword",produces = "application/json")
    public ResponseEntity<CustomResponse> updatePassword(@RequestBody UserDTO userDTO){
        try {
            return this.userService.updatePassword(userDTO);
        }catch (Exception e){
            log.error("Error"+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal"));
        }
    }
    @PostMapping(value = "/recoverPassword",produces = "application/json")
    public ResponseEntity<CustomResponse> recoverPassword(@RequestBody MailDTO mailDTO){
        try {
           return mailService.recoverPassword(mailDTO);
        }catch (Exception e){
            log.error("Algo sucedio al recuperar la contraseña");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal al recuperar la contraseña"));
        }
    }
    @PostMapping(value = "/validateRecoverPassword",produces = "application/json")
    public ResponseEntity<CustomResponse> validateRecoverPassword(@RequestBody MailDTO mailDTO){
        try {
            return this.userService.verifyRecoverPassword(mailDTO);
        }catch (Exception e){
            log.error("Algo sucedio al recuperar la contraseña");
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true,HttpStatus.INTERNAL_SERVER_ERROR.value(),"Algo salio mal al recuperar la contraseña"));
        }
    }
}

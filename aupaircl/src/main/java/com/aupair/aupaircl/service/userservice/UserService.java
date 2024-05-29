package com.aupair.aupaircl.service.userservice;

import com.aupair.aupaircl.controller.usercontroller.userDTO.UserDTO;
import com.aupair.aupaircl.model.emailverification.EmailVerificationRepository;
import com.aupair.aupaircl.model.rol.Rol;
import com.aupair.aupaircl.model.rol.RolRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class UserService {
private final UserRepository userRepository;
private final EmailVerificationRepository emailVerificationRepository;
private final RolRepository rolesRepository;
private static final String AuPairType= "aupair";
    HashMap<String, Integer> intentValid = new HashMap<>();
    private final MailService mailService;

@Autowired
public UserService(UserRepository userRepository,EmailVerificationRepository emailVerificationRepository,
                   RolRepository rolesRepository, MailService mailService){
    this.userRepository = userRepository;
    this.emailVerificationRepository = emailVerificationRepository;
    this.rolesRepository = rolesRepository;
    this.mailService = mailService;
}
    @Transactional(rollbackFor={SQLException.class})
    public ResponseEntity<CustomResponse> registerUser(UserDTO userDTO){
    try {
            User user = new User();
            if(this.userRepository.existsByEmail(userDTO.getEmail())){
                return ResponseEntity.ok(new CustomResponse("User already exists",400,true,null));
            }
            if(Objects.equals(userDTO.getIsType(), AuPairType)){
                Rol rol = this.rolesRepository.findByRoleName(AuPairType).get();
                if( rol == null){
                    return ResponseEntity.ok(new CustomResponse("Role not found",400,true,null));
                }
                user.setEmail(userDTO.getEmail());
                user.setPassword(userDTO.getPassword());
                user.setRole(rol);
                user.setUsername(userDTO.getUsername());
                user.setResetToken("");
                user.setResetTokenExpires(new Date());
                     this.userRepository.save(user);
                     this.mailService.verifyAccount(userDTO.getEmail());
                    return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.OK.value(), "Usuario registrado"));

            }
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.OK.value(), "Es famlia"));

    }catch (Exception ex){
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio en el servidor"));
    }
    }
}

package com.aupair.aupaircl.service.authservice;

import com.aupair.aupaircl.controller.authcontroller.authdto.AuthRequest;
import com.aupair.aupaircl.model.profile.ProfileRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.security.service.JwtService;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final UserRepository userAccountRepository;
    private final ProfileRepository profileRepository;
    private final AuthenticationManager manager;
    private final JwtService provider;
    private static final String RESPONSE_INVALID_CREDENTIALS = "Credenciales invalidas";
    private final MailService mailService;

    @Autowired
    public AuthService(UserRepository userAccountRepository, MailService mailService, AuthenticationManager manager,
                       JwtService provider,ProfileRepository profileRepository) {
        this.userAccountRepository = userAccountRepository;
        this.manager = manager;
        this.provider = provider;
        this.mailService= mailService;
        this.profileRepository = profileRepository;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> login(AuthRequest authRequest) {
        try {
            Optional<User> userAccount = userAccountRepository.findByEmail(authRequest.getEmail());
            if (userAccount.isEmpty()){
                log.error("Usuario invalido");
                return ResponseEntity.status(600).body(
                        new CustomResponse(true,600, RESPONSE_INVALID_CREDENTIALS));
            }
            if(userAccount.isPresent() &&  Boolean.FALSE.equals(userAccount.get().isEmailVerified())){
                log.error("Usuario no verificado");
                return ResponseEntity.status(HttpStatus.OK  ).body(new CustomResponse(true,599,"Cuenta no verificada"));
            }
            if (userAccount.isPresent() && Boolean.TRUE.equals(userAccount.get().isLocked())){
                log.error("Usuario bloqueado");
                return ResponseEntity.status(601).body(
                        new CustomResponse(true, 601, "Cuenta bloqueada"));
            }


            String token = authentication(authRequest);
            if (token == null) {
                if(userAccount.get().getRole().getRoleName().equals("family") || userAccount.get().getRole().getRoleName().equals("aupair") && userAccount.get().getFailedAttempts() >= 3){
                    userAccount.get().setLocked(true);
                    userAccountRepository.saveAndFlush(userAccount.get());
                    mailService.blockedAccountByFailedIntents(userAccount.get().getEmail());
                }else{
                    userAccount.get().setFailedAttempts(userAccount.get().getFailedAttempts() + 1);
                    userAccountRepository.saveAndFlush(userAccount.get());
                }

                return ResponseEntity.status(600).body(
                        new CustomResponse(true, 600, RESPONSE_INVALID_CREDENTIALS));
            }
            userAccount.get().setResetToken(token);
            userAccount.get().setLastLogin(LocalDateTime.now());
            userAccountRepository.saveAndFlush(userAccount.get());
            if(!this.profileRepository.findByUser_Email(userAccount.get().getEmail()).isPresent()){
                log.error("Usuario sin perfil registrado");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse("Primero debes completar tu perfil",606,true, token));
            }
            return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, token).body(
                    new CustomResponse("Login exitoso", 201, false, token));
        } catch (Exception e) {
            log.error("Error al hacer login", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new CustomResponse( true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error intentando hacer login"));
        }
    }


    private String authentication(AuthRequest authRequest) {
        try {
            Authentication auth = manager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            return provider.generateToken(auth);
        } catch (BadCredentialsException e) {
            log.error(RESPONSE_INVALID_CREDENTIALS);
            return null;
        }
    }

}

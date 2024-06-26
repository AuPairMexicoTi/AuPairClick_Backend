package com.aupair.aupaircl.service.authservice;

import com.aupair.aupaircl.controller.authcontroller.authdto.AuthRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final UserRepository userAccountRepository;
    private final AuthenticationManager manager;
    private final JwtService provider;
    private static final String RESPONSE_INVALID_CREDENTIALS = "Credenciales invalidas";
    private final MailService mailService;

    @Autowired
    public AuthService(UserRepository userAccountRepository, MailService mailService, AuthenticationManager manager, JwtService provider) {
        this.userAccountRepository = userAccountRepository;
        this.manager = manager;
        this.provider = provider;
        this.mailService= mailService;
    }

    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<CustomResponse> login(AuthRequest authRequest) {
        try {
            Optional<User> userAccount = userAccountRepository.findByEmail(authRequest.getEmail());
            if(userAccount.isPresent() &&  Boolean.FALSE.equals(userAccount.get().getEmailVerified())){
                log.error("Usuario no verificado");
                return ResponseEntity.status(599).body(new CustomResponse(true,599,"Cuenta no verificada"));
            }
            if (userAccount.isPresent() && Boolean.TRUE.equals(userAccount.get().getIsLocked())){
                log.error("Usuario bloqueado");
                return ResponseEntity.status(601).body(
                        new CustomResponse(true, 601, "Cuenta bloqueada"));
            }

            if (userAccount.isPresent()) {
                String token = authentication(authRequest);
                if (token == null) {
                    if(userAccount.isPresent() && userAccount.get().getRole().getRoleName().equals("family") || userAccount.get().getRole().getRoleName().equals("aupair") && userAccount.get().getFailedAttempts() >= 3){
                        userAccount.get().setIsLocked(true);
                        userAccountRepository.saveAndFlush(userAccount.get());
                        mailService.blockedAccountByFailedIntents(userAccount.get().getEmail());
                    }else{
                        userAccount.get().setFailedAttempts(userAccount.get().getFailedAttempts() + 1);
                        userAccountRepository.saveAndFlush(userAccount.get());
                    }

                    return ResponseEntity.status(600).body(
                            new CustomResponse(true, 600, "Credenciales incorrectas"));
                }
                userAccount.get().setResetToken(token);
                userAccount.get().setLastLogin(new Date());
                userAccountRepository.saveAndFlush(userAccount.get());
                return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.AUTHORIZATION, token).body(
                        new CustomResponse("Login exitoso", 201, false, token));
            } else {
                log.error("Usuario invalido");
                return ResponseEntity.status(600).body(
                        new CustomResponse(true,600, "Credenciale invalidas"));
            }
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

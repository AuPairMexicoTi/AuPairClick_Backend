package com.aupair.aupaircl.service.mailservice;

import com.aupair.aupaircl.controller.mailcontroller.maildto.MailDTO;
import com.aupair.aupaircl.model.emailverification.EmailVerification;
import com.aupair.aupaircl.model.emailverification.EmailVerificationRepository;
import com.aupair.aupaircl.model.user.User;
import com.aupair.aupaircl.model.user.UserRepository;
import com.aupair.aupaircl.utils.CustomResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
@Slf4j
@Service
@Transactional
public class MailService {
    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private Map<String, String> recoveryCodes = new ConcurrentHashMap<>();
    private String errorMessage = "Usuario invalido";
    private final JavaMailSender javaMailSender;
    private Environment env;
    SecureRandom random = new SecureRandom();
    @Autowired
    public MailService(UserRepository userRepository,Environment env,EmailVerificationRepository emailVerificationRepository,JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
        this.emailVerificationRepository = emailVerificationRepository;
        this.env = env;
    }

    @Transactional
    public ResponseEntity<CustomResponse> validateCodeEmail(MailDTO mailDTO) {
        try {
            Optional<EmailVerification> verification = this.emailVerificationRepository.findByUser_Email(mailDTO.getEmail());
            if (verification.isPresent()) {
                if (mailDTO.getCode().equals( verification.get().getVerificationToken())) {
                    Optional<User> user = this.userRepository.findByEmail(mailDTO.getEmail());
                    user.get().setEmailVerified(true);
                    verification.get().setExpiresAt(new Date());
                    verification.get().setVerificationToken("");
                    this.userRepository.save(user.get());
                    this.emailVerificationRepository.save(verification.get());
                    return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.OK.value(), "Codigo validado"));
                }else{
                    log.info("Codigo expirado");
                    return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Codigo invalido"));
                }
            }else{
                log.info("Codigo expirado");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "Codigo invalido"));
            }

        }catch (Exception e){
            log.error("Algo salio mal en la verificacion del codigo de email "+e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
        }

    }
    @Transactional
    public ResponseEntity<CustomResponse> sendCodeEmail(MailDTO mailDTO) {
        String code = genereteRandomCode();
        Optional<User> userSaved = this.userRepository.findByEmail(mailDTO.getEmail());
        if (userSaved.isPresent()) {
            if (!userSaved.get().getEmailVerified()) {
                EmailVerification emailVerificationSaved = new EmailVerification();
                emailVerificationSaved.setUser(userSaved.get());
                emailVerificationSaved.setVerificationToken(code);
                emailVerificationSaved.setExpiresAt(new Date());
                this.emailVerificationRepository.saveAndFlush(emailVerificationSaved);
                log.info("Codigo enviado");
                String html = """
                        <html>
                        <head>
                            <style>
                                body {
                                    background-color: #F5F5F7;
                                    padding: 20px;
                                    line-height: 1.6;
                                    font-family: Arial, sans-serif;
                                }
                                .text {
                                    font-size: 24px;
                                    text-align: justify;
                                    color: black;
                                }
                                .header {
                                    font-size: 16px;
                                    text-align: justify;
                                    color: #8d8c8c;
                                }
                                .container-fluid {
                                    margin: 0 auto;
                                    max-width: 600px;
                                    background-color: white;
                                    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
                                    border-radius: 26px;
                                }
                                .row {
                                    padding: 30px;
                                    padding-block: 60px;
                                }
                                .color-font {
                                    width: 100%%;
                                    height: auto;
                                }
                                h1 {
                                    color: #ED8003;
                                }
                                .size {
                                    width: 150px;
                                    height: auto;
                                }
                                a {
                                    margin-left: 10px;
                                    margin-right: 10px;
                                    border: 1px solid black;
                                    padding: 15px;
                                    border-radius: 16px;
                                    letter-spacing: 50px;
                                    padding-left: 50px;
                                }
                                .container-code {
                                    text-align: center;
                                    padding: 50px;
                                }
                            </style>
                        </head>
                        <body>
                            <div class="container-fluid">
                                <div class="row" style="text-align:center;">
                                    <img class="size" src="https://fhweovj.stripocdn.email/content/guids/CABINET_1b64288a36c96ad48cda203d3abb3b684f230cc2a18e4f2ba8ee4884477de08c/images/marca_au_pair_click.png" alt="Logo.png">
                                    <div style="text-align:center;">
                                        <h1  style="color:#6600FF;">Hola, %s</h1>
                                        <h2 class="text">Confirma tu correo </h2>
                                        <p>Codigo de verificacion</p<
                                    </div>
                                    <div class="container-code">
                                        <a>%s</a>
                                    </div>
                                    <div class="color-font">
                                        <p class="header">Si no solicitaste este correo, por favor ignóralo.</p>
                                        <strong> Este es un correo generado automáticamente, por favor no respondas a este mensaje.</strong>
                                    </div>
                                </div>
                            </div>
                        </body>
                        </html>
                        """;

                String messageFormat = String.format(
                        html,
                        userSaved.get().getUsername(),
                        code
                );


                return sendEmail(mailDTO.getEmail(), "Codigo de verificación ", messageFormat);
            }else{
                log.info("El usuario ya esta verificado");
                return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "El usuario ya esta verificado"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.BAD_REQUEST.value(), "El usuario no es valido"));
        }
    }
    @Transactional
    public ResponseEntity<CustomResponse> verifyAccount(String email){
        String code = genereteRandomCode();
        Optional<User> userSaved = this.userRepository.findByEmail(email);
        if(userSaved.isPresent()) {
            EmailVerification emailVerificationSaved = new EmailVerification();
            emailVerificationSaved.setUser(userSaved.get());
            emailVerificationSaved.setVerificationToken(code);
            emailVerificationSaved.setExpiresAt(new Date());
            this.emailVerificationRepository.save(emailVerificationSaved);
        }

        User user = userRepository.findByEmail(email).get();
        String username = user != null ? user.getUsername() : "Querid@ usuario";

        if (code.length() < 4) {
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(false, HttpStatus.OK.value(), errorMessage));
        }

        String html = """
        <html>
        <head>
            <style>
                body {
                    background-color: #F5F5F7;
                    padding: 20px;
                    line-height: 1.6;
                    font-family: Arial, sans-serif;
                }
                .text {
                    font-size: 24px;
                    text-align: justify;
                    color: black;
                }
                .header {
                    font-size: 16px;
                    text-align: justify;
                    color: #8d8c8c;
                }
                .container-fluid {
                    margin: 0 auto;
                    max-width: 600px;
                    background-color: white;
                    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
                    border-radius: 26px;
                }
                .row {
                    padding: 30px;
                    padding-block: 60px;
                }
                .color-font {
                    width: 100%%;
                    height: auto;
                }
                h1 {
                    color: #ED8003;
                }
                .size {
                    width: 150px;
                    height: auto;
                }
                a {
                    margin-left: 10px;
                    margin-right: 10px;
                    border: 1px solid black;
                    padding: 15px;
                    border-radius: 16px;
                    letter-spacing: 50px;
                    padding-left: 50px;
                }
                .container-code {
                    text-align: center;
                    padding: 50px;
                }
            </style>
        </head>
        <body>
            <div class="container-fluid">
                <div class="row" style="text-align:center;">
                    <img class="size" src="https://fhweovj.stripocdn.email/content/guids/CABINET_1b64288a36c96ad48cda203d3abb3b684f230cc2a18e4f2ba8ee4884477de08c/images/marca_au_pair_click.png" alt="Logo.png">
                    <div style="text-align:center;">
                        <h1  style="color:#6600FF;">Hola, %s</h1>
                        <h2 class="text">Confirma tu correo </h2>
                        <p>Gracias por registrarte en nuestra plataforma. Para completar tu registro, por favor ingresa el siguiente código de verificación en nuestra página</p<
                    </div>
                    <div class="container-code">
                        <a>%s</a>
                    </div>
                    <div class="color-font">
                        <p class="header">Si no solicitaste este correo, por favor ignóralo.</p>
                        <strong> Este es un correo generado automáticamente, por favor no respondas a este mensaje.</strong>
                    </div>
                </div>
            </div>
        </body>
        </html>
        """;

        String messageFormat = String.format(
                html,
                username,
                code
        );

        recoveryCodes.put(email, code);
        scheduleCodeRemoval(email, 20, TimeUnit.MINUTES);
        return sendEmail(email, "Bienvenid@! Verifica tu cuenta ", messageFormat);
    }
    public ResponseEntity<CustomResponse> sendEmail(String email, String title, String html) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(Objects.requireNonNull(env.getProperty("spring.mail.username")));
            helper.setTo(email);
            helper.setSubject(title);
            helper.setText(html,true);
            javaMailSender.send(message);
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse( false, HttpStatus.OK.value(), "Send email"));
        }  catch (MessagingException e) {
            log.error("Error de conexion");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomResponse( true, HttpStatus.NOT_FOUND.value(), "Error connexion"));
        }
    }

    private void scheduleCodeRemoval(String email, long delay, TimeUnit unit) {
        if (delay <= 0) {
            throw new IllegalArgumentException("El retraso debe ser un valor positivo.");
        }

        java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                unit.sleep(delay);
                recoveryCodes.remove(email);
            } catch (InterruptedException e) {
                log.error("¡Interrumpido!", e);
                Thread.currentThread().interrupt();
            }
        });
        executor.shutdown();
    }

    public String genereteRandomCode () {
        int randomCode = 1000 + random.nextInt(9000);
        return String.valueOf(randomCode);
    }
}

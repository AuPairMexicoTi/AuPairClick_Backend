package com.aupair.aupaircl.controller.mailcontroller;

import com.aupair.aupaircl.controller.mailcontroller.maildto.MailDTO;
import com.aupair.aupaircl.service.mailservice.MailService;
import com.aupair.aupaircl.utils.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/mail")
@CrossOrigin(origins = {"http://localhost:5173/"})
@Slf4j
@RestController
public class MailController {
    private final MailService mailService;
    private static final String INTERNALSERVERERROR = "Algo ocurrio en el servidor";

    @Autowired
    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping(value = "/validateCode", produces = "application/json")
    public ResponseEntity<CustomResponse> validateCode(@RequestBody MailDTO mailDTO) {
        try {
            return this.mailService.validateCodeEmail(mailDTO);

        } catch (Exception e) {
            log.error("Error en validate code " + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al validar"));
        }
    }

    @PutMapping(value = "/sendCode", produces = "application/json")
    public ResponseEntity<CustomResponse> sendCode(@RequestBody MailDTO mailDTO) {
        try {
            return this.mailService.sendCodeEmail(mailDTO);
        } catch (Exception e) {
            log.error("Error" + e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo salio mal"));
        }
    }
}
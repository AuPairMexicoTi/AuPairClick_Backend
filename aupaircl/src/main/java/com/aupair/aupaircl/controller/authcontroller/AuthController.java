package com.aupair.aupaircl.controller.authcontroller;

import com.aupair.aupaircl.controller.authcontroller.authdto.AuthRequest;
import com.aupair.aupaircl.service.authservice.AuthService;
import com.aupair.aupaircl.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost"})
public class AuthController {
    private static AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping(value = "/login",produces = "application/json")
    public ResponseEntity<CustomResponse> login(@RequestBody AuthRequest authRequest){
        try {
            return authService.login(authRequest);

        }catch (Exception e){
            return new ResponseEntity<>(new CustomResponse(
                    true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Algo sucedio al iniciar sesion"), HttpStatus.OK);        }
        }

}

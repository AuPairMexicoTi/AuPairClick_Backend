package com.aupair.aupaircl.controller.usercontroller;

import com.aupair.aupaircl.controller.usercontroller.userdto.UserDTO;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
}

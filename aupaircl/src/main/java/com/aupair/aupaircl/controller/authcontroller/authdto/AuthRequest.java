package com.aupair.aupaircl.controller.authcontroller.authdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequest {
    @Email(message = "Debe ser un correo valido")
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}

package com.aupair.aupaircl.model.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserEmailDto {
    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@gmail\\.[a-zA-Z]{2,}$", message = "El correo electrónico debe ser de Gmail")
    @NotEmpty(message = "El correo no puede estar en blanco")
    String email;
}

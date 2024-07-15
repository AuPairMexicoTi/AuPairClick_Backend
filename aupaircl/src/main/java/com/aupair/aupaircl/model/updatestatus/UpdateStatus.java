package com.aupair.aupaircl.model.updatestatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class UpdateStatus {
    @NotEmpty(message = "El nombre no puede estar en blanco")
    private String data;
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "El status no debe contener caracteres especiales")
    @NotEmpty(message = "El status no puede estar en blanco")
    private String status;
}
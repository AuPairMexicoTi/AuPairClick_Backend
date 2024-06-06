package com.aupair.aupaircl.controller.approvalcontroller.approvaldto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ApprovalDTO {
    private Boolean isApproved; //El dto se reutiliza para las diferentes aprovaciones, para el caso de la cuenta se setea el valor al campo isLocked, por lo que el valor que se debe enviar es false
    private String message;
    private String email;
}

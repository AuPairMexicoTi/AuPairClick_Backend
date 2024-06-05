package com.aupair.aupaircl.controller.approvalcontroller.approvaldto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class ApprovalDTO {
    private Boolean isApproved;
    private String message;
    private String email;
}

package com.aupair.aupaircl.controller.mailcontroller.maildto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class MailDTO {
    private String email;
    private String code;
}

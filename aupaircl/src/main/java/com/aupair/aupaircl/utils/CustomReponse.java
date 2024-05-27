package com.aupair.aupaircl.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomReponse {
Object data;
boolean error;
int statusCode;
String message;

    public CustomReponse(String message, int statusCode, boolean error, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.error = error;
        this.data = data;
    }

}

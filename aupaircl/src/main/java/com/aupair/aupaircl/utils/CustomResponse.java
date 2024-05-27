package com.aupair.aupaircl.utils;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CustomResponse {
Object data;
boolean error;
int statusCode;
String message;

    public CustomResponse(String message, int statusCode, boolean error, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.error = error;
        this.data = data;
    }
    public CustomResponse(boolean error,int statusCode,  String message) {
        this.error = error;
        this.statusCode = statusCode;
        this.message = message;
    }

}

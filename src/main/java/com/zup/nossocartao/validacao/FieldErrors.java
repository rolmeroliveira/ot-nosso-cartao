package com.zup.nossocartao.validacao;

import java.time.LocalDateTime;

public class FieldErrors extends StandardError {

    private String field;
    private String fieldsMessage;

    public FieldErrors(LocalDateTime timeError, Integer statusCode, String status, String details, String field, String fieldsMessage) {
        super(timeError, statusCode, status, details);
        this.field = field;
        this.fieldsMessage = fieldsMessage;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return fieldsMessage;
    }
}

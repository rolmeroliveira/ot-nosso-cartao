package com.zup.nossocartao.validacao;

import java.time.LocalDateTime;

public class CommonException extends RuntimeException{

    //---------------
    private String field;
    private String msg;
    private LocalDateTime thrownTime =  LocalDateTime.now();

    //---------------
    public CommonException(String field , String msg) {
        this.field = field;
        this.msg = msg;
    }

    //---------------
    public String getField() {
        return field;
    }
    public String getMsg() {
        return msg;
    }
    public LocalDateTime getThrownTime() {
        return thrownTime;
    }
}

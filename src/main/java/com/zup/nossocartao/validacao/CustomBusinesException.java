package com.zup.nossocartao.validacao;

public class CustomBusinesException extends CommonException {

    public CustomBusinesException(String field, String msg) {
        super(field, msg);
    }
}

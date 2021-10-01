package com.zup.nossocartao.validacao;

public class CustomNotFoundException extends CommonException {

    public CustomNotFoundException(String field, String msg) {
        super(field, msg);
    }
}

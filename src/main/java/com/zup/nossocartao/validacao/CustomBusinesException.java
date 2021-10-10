package com.zup.nossocartao.validacao;

public class CustomBusinesException extends CommonException {

    //O handler captura, lançando Unprocesable entity - 422
    public CustomBusinesException(String field, String msg) {
        super(field, msg);
    }
}

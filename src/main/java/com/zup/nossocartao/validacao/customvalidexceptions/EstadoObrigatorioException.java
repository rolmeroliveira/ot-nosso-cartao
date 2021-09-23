package com.zup.nossocartao.validacao.customvalidexceptions;

import org.springframework.http.converter.HttpMessageNotReadableException;

public class EstadoObrigatorioException extends HttpMessageNotReadableException {
    public EstadoObrigatorioException(String mensagemErro) {
        super(mensagemErro);
    }
}

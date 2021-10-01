package com.zup.nossocartao.bloqueios;

public class BloqueioCartaoRequest {
    private Long idCartao;

    public BloqueioCartaoRequest() {
    }

    public BloqueioCartaoRequest(Long idCartao) {
        this.idCartao = idCartao;
    }

    public Long getIdCartao() {
        return idCartao;
    }
}

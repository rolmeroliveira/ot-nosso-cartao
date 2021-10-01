package com.zup.nossocartao.bloqueios;

import javax.validation.constraints.NotBlank;

public class BloqueioCartaoAvisoRequest {

    @NotBlank
    private String sistema;

    public BloqueioCartaoAvisoRequest() {}

    public BloqueioCartaoAvisoRequest(@NotBlank String sistema) {
        this.sistema = sistema;
    }

    public String getSistema() {
        return sistema;
    }

    public void setSistemaResponsavel(String sistema) {
        this.sistema = sistema;
    }
}

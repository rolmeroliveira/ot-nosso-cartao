package com.zup.nossocartao.avisoviagem;

import java.time.format.DateTimeFormatter;

public class AvisoViagemResponse {

    private String identificadorCartao;
    private String destino;
    private String dataTerminoViagem;


    public AvisoViagemResponse(AvisoViagem avisoViagem) {
        this.identificadorCartao = avisoViagem.getCartao().getNumero();
        this.destino = avisoViagem.getDestino();
        this.dataTerminoViagem = avisoViagem.getDataTerminoViagem().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public String getIdentificadorCartao() {
        return identificadorCartao;
    }

    public String getDestino() {
        return destino;
    }

    public String getDataTerminoViagem() {
        return dataTerminoViagem;
    }
}

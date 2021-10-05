package com.zup.nossocartao.avisoviagem;

import com.zup.nossocartao.cartao.Cartao;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AvisoViagemRequest {

    @NotNull
    private String identificadorCartao;
    @NotNull
    private String destino;
    @NotNull
    @Future(message = "A data do fim da viagem precisa ser futura")
    private String dataTerminoViagem;

    public AvisoViagemRequest() {
    }

    public AvisoViagemRequest(String identificadorCartao, String destino, String dataTerminoViagem) {
        this.identificadorCartao = identificadorCartao;
        this.destino = destino;
        this.dataTerminoViagem = dataTerminoViagem;

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

    public AvisoViagem toModel(Cartao cartao, String ipAviso, String userAgent) {
        LocalDate localDate = LocalDate.parse(this.dataTerminoViagem, DateTimeFormatter.ofPattern("dd/MM/yyyy"));


        AvisoViagem avisoViagem = new AvisoViagem(
                cartao,
                this.destino,
                localDate,
                ipAviso,
                userAgent
        );
        return avisoViagem;
    }


    @Override
    public String toString() {
        return "AvisoViagemRequest{" +
                "identificadorCartao='" + identificadorCartao + '\'' +
                ", destino='" + destino + '\'' +
                ", dataTerminoViagem='" + dataTerminoViagem + '\'' +
                '}';
    }
}

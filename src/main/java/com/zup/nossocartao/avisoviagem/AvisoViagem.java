package com.zup.nossocartao.avisoviagem;

import com.zup.nossocartao.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destino;
    @Future(message = "A data do fim da viagem precisa ser futura")
    private LocalDate dataTerminoViagem;
    @NotNull
    private LocalDateTime dataHoraAviso = LocalDateTime.now();
    @NotNull
    private String ipAviso;
    @NotNull
    private String userAgent;
    @ManyToOne
    private Cartao cartao;

    public AvisoViagem() {
    }

    public AvisoViagem(Cartao cartao, String destino, LocalDate dataTermino, String iPDoAviso, String userAgent) {
        this.cartao = cartao;
        this.destino = destino;
        this.dataTerminoViagem   = dataTermino;
        this.ipAviso = iPDoAviso;
        this.userAgent = userAgent;

    }

    public Long getId() {
        return id;
    }

    public String getDestino() {
        return destino;
    }

    public LocalDate getDataTerminoViagem() {
        return dataTerminoViagem;
    }

    public LocalDateTime getDataHoraAviso() {
        return dataHoraAviso;
    }

    public String getIpAviso() {
        return ipAviso;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Cartao getCartao() {
        return cartao;
    }
}

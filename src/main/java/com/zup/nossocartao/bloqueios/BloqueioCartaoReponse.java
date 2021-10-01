package com.zup.nossocartao.bloqueios;

import com.zup.nossocartao.cartao.Cartao;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartaoReponse {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataHoraBloqueio = LocalDateTime.now();
    private String ipOrigem;
    private String userAgent;


    public BloqueioCartaoReponse() {
    }


    public BloqueioCartaoReponse(LocalDateTime dataHoraBloqueio, String ipOrigem, String userAgent) {
        this.dataHoraBloqueio = dataHoraBloqueio;
        this.ipOrigem = ipOrigem;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataHoraBloqueio() {
        return dataHoraBloqueio;
    }

    public String getIpOrigem() {
        return ipOrigem;
    }

    public String getUserAgent() {
        return userAgent;
    }
}

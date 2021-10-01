package com.zup.nossocartao.bloqueios;

import com.zup.nossocartao.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class BloqueioCartao {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @ManyToOne
    private Cartao cartao;
    private LocalDateTime dataHoraBloqueio = LocalDateTime.now();
    private String ipOrigem;
    private String userAgent;
    private BloqueioCartaoStatus bloqueiosCartaoStatus = BloqueioCartaoStatus.ATIVO;


    public BloqueioCartao() {
    }

    public BloqueioCartao(Cartao cartao, String ipOrigem, String userAgent) {
        this.cartao = cartao;
        this.ipOrigem = ipOrigem;
        this.userAgent = userAgent;
    }

    public Long getId() {
        return id;
    }

    public Cartao getCartao() {
        return cartao;
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

    public BloqueioCartaoStatus getBloqueioCartaoStatus() {
        return bloqueiosCartaoStatus;
    }
}

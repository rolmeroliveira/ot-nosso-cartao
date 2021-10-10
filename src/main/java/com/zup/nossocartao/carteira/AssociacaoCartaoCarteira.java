package com.zup.nossocartao.carteira;

import com.zup.nossocartao.cartao.Cartao;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AssociacaoCartaoCarteira {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    private Cartao cartao;
    private String carteira;


    private String ipAssociacao;
    private  String userAgentAssociacao;
    private LocalDateTime momentoAssociacao = LocalDateTime.now();

    public AssociacaoCartaoCarteira() {
    }

    public AssociacaoCartaoCarteira(Cartao cartao, String carteira, String ipAssociacao, String userAgentAssociacao) {
        this.cartao = cartao;
        this.carteira = carteira;
        this.ipAssociacao = ipAssociacao;
        this.userAgentAssociacao = userAgentAssociacao;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public String getCarteira() {
        return carteira;
    }
}

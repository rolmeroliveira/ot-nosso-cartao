package com.zup.nossocartao.carteira;

import com.zup.nossocartao.cartao.Cartao;

public class AssociacaoCartaoCarteiraRequest {

    private String email;
    private String carteira;

    public AssociacaoCartaoCarteira toModel (Cartao cartao, String ipClient, String user_agent){
        AssociacaoCartaoCarteira associacaoCartaoCarteira = new AssociacaoCartaoCarteira(
            cartao,
            this.carteira,
            ipClient,
            user_agent);

        return associacaoCartaoCarteira;
    }

    public String getCarteira() {
        return carteira;
    }

    public String getEmail() {
        return email;
    }
}

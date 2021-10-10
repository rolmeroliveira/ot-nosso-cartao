package com.zup.nossocartao.carteira;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "associa-cartao-carteira", url = "http://localhost:8888/api/cartoes")
public interface AssociacaoCartaoCarteiraClient {

    @PostMapping("/{id}/carteiras")
    public AssociaCartaoCarteiraBancoResponse requisitaAssociacaoSistemaBancario(@PathVariable("id") String id,
                                                                AssociaCartaoCarteiraBancoRequest associaCartaoCarteiraBancoRequest);

}

package com.zup.nossocartao.proposta.analise;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "AnaliseProposta", url =  "http://localhost:9999/api/solicitacao")
public interface AnalisePropostaClient {

    @PostMapping
    AnaliseResponse analisar(AnaliseRequest analiseRequest);

}

package com.zup.nossocartao.bloqueios;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "avisa-bloqueio-cartao", url = "http://localhost:8888/api/cartoes")
public interface BloqueioCartaoAvisoClient {

    @PostMapping("/{id}/bloqueios")
    public BloqueioCartaoAvisoResponse avisarSistemalegado(@PathVariable("id") String id,
                                                           BloqueioCartaoAvisoRequest bloqueioCartaoAvisoRequest);

}

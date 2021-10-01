package com.zup.nossocartao.cartao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "vincula-cartao", url = "http://localhost:8888/api/cartoes")
public interface VinculaCartaoClient {
        @PostMapping
        VinculaCartaoResponse associaCartao(VinculaCartaoRequest request);


}

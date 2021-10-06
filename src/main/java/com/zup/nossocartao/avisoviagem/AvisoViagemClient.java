package com.zup.nossocartao.avisoviagem;


import com.zup.nossocartao.bloqueios.BloqueioCartaoAvisoRequest;
import com.zup.nossocartao.bloqueios.BloqueioCartaoAvisoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "avisa-aviso-viagem", url = "http://localhost:8888/api/cartoes")
public interface AvisoViagemClient {

    @PostMapping("/{id}/avisos")
    public AvisoViagemAvisoResponse avisarSistemalegado(@PathVariable("id") String id,
                                                           AvisoViagemAvisoRequest avisoViagemAvisoRequest);

}

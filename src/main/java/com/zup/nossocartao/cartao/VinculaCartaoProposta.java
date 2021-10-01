package com.zup.nossocartao.cartao;


import com.zup.nossocartao.proposta.Proposta;
import com.zup.nossocartao.repository.PropostaRepository;
import feign.FeignException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class VinculaCartaoProposta {

    private final PropostaRepository propostaRepository;
    private final VinculaCartaoClient vinculaCartaoClient;

    public VinculaCartaoProposta(PropostaRepository propostaRepository, VinculaCartaoClient vinculaCartaoClient) {
        this.propostaRepository = propostaRepository;
        this.vinculaCartaoClient = vinculaCartaoClient;
    }

    @Scheduled(fixedDelayString = "${periodicidade.vincula-cartao}")
    public void vincula() {

        List<Proposta> propostas = propostaRepository.findBystatusAndCartaoIsNull(StatusAssociaCartao.ELEGIVEL);

        for (Proposta p: propostas) {
            VinculaCartaoRequest request = new VinculaCartaoRequest(p);
            try {
                VinculaCartaoResponse response = vinculaCartaoClient.associaCartao(request);
                Cartao cartao = response.toModel(p);
                p.vinculaCartao(cartao);
                propostaRepository.save(p);
            } catch (FeignException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.zup.nossocartao.proposta.analise;
import com.zup.nossocartao.proposta.PropostaRequest;

public class AnaliseRequest {

    private String documento;
    private String nome;
    private String idProposta;

    public AnaliseRequest(PropostaRequest propostaRequest) {
        this.documento = propostaRequest.getDocumento();
        this.nome = propostaRequest.getNome();
        this.idProposta = "1";
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }
}

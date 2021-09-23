package com.zup.nossocartao.proposta.analise;

public class AnaliseResponse {

    private String documento;
    private String nome;
    private String resultadoSolicitacao;
    private String idProposta;

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setResultadoSolicitacao(String resultadoSolicitacao) {
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public void setIdProposta(String idProposta) {
        this.idProposta = idProposta;
    }
}

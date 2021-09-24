package com.zup.nossocartao.proposta.cartao;

import com.zup.nossocartao.proposta.Proposta;

public class VinculaCartaoRequest {

        private String documento;

        private String nome;

        private String idProposta;

        @Deprecated
        public VinculaCartaoRequest() {
        }

        public VinculaCartaoRequest(Proposta proposta) {
            this.documento = proposta.getDocumento();
            this.nome = proposta.getNome();
            this.idProposta = proposta.getId().toString();
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

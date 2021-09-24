package com.zup.nossocartao.proposta.cartao;

import com.zup.nossocartao.proposta.Proposta;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VinculaCartaoResponse {


        @NotBlank
        private String id;

        @NotNull
        private LocalDateTime emitidoEm;

        @NotNull
        private BigDecimal limite;

        public String getId() {
            return id;
        }

        public LocalDateTime getEmitidoEm() {
            return emitidoEm;
        }

        public BigDecimal getLimite() {
            return limite;
        }

        public Cartao toModel(Proposta proposta) {
            return new Cartao(proposta, id, emitidoEm, limite);
        }
}

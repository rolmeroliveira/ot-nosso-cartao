package com.zup.nossocartao.proposta;

import java.math.BigDecimal;
import java.util.Optional;

public class PropostaResponse {
    //Aqui não tem validação. Não vou validar o dado que eu mesmo entrego
    private Long id;
    private String documento;
    private String email;
    private String nome;
    private String endereco;
    private BigDecimal salario;

    //Construtor transforma Model em Response
    public PropostaResponse(Proposta proposta) {
        this.id = proposta.getId();
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario() ;
    }

    //Os getters são necessários pra o jackson fazer o parse pra json
    //na hora de enviar o response
    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return documento;
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}

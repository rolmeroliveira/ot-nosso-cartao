package com.zup.nossocartao.proposta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.nossocartao.proposta.cartao.StatusAssociaCartao;
import com.zup.nossocartao.validacao.CpfOuCnpjValido;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PropostaRequest {


    //Documento do solicitante deve ser obrigatório e válido
    @JsonProperty
    @NotBlank
    @CpfOuCnpjValido(message = "Fornato incorreto de CPF ou CNPJ")
    @Column(unique=true)
    private String documento;

    //E-mail não pode ser vazio, nulo ou inválido
    @JsonProperty
    @NotNull
    @NotBlank
    @Email
    private String email;

    //Nome não pode ser vazio ou nulo
    @JsonProperty
    @NotBlank
    @NotNull
    private String nome;

    //Endereço não pode ser vazio ou nulo
    @JsonProperty
    @NotBlank
    @NotNull
    private String endereco;

    //Salário bruto não pode ser vazio, nulo ou negativo
    @JsonProperty
    @NotNull
    @Positive
    private BigDecimal salario;


    public PropostaRequest() {
    }

    public PropostaRequest(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Proposta toModel(StatusAssociaCartao statusAssociaCartao) {
        Proposta proposta = new Proposta(
            this.documento,
            this.email,
            this.nome,
            this.endereco,
            this.salario,
            statusAssociaCartao
        );
        return proposta;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
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

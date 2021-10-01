package com.zup.nossocartao.proposta;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zup.nossocartao.cartao.Cartao;
import com.zup.nossocartao.cartao.StatusAssociaCartao;
import com.zup.nossocartao.validacao.CpfOuCnpjValido;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


@Entity
@Table(
        name="proposta",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"documento"}))
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Documento do solicitante deve ser obrigatório e válido

    @JsonProperty
    @NotBlank
    @CpfOuCnpjValido(message = "Fornato incorreto de CPF ou CNPJ")
    private String documento;

    //E-mail não pode ser vazio, nulo ou inválido
    @NotNull
    @NotBlank
    @Email
    private String email;

    //Nome não pode ser vazio ou nulo
    @NotBlank
    @NotNull
    private String nome;

    //Endereço não pode ser vazio ou nulo
    @NotBlank
    @NotNull
    private String endereco;

    //Salário bruto não pode ser vazio, nulo ou negativo
    @NotNull
    @Positive
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private StatusAssociaCartao status;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "proposta")
    private Cartao cartao;


    public Proposta() {
    }

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario, StatusAssociaCartao statusAssociaCartao) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
        this.status = statusAssociaCartao;
    }

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

    public StatusAssociaCartao getStatus() {
        return status;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void vinculaCartao(Cartao cartao) {
        this.cartao = cartao;
    }
}

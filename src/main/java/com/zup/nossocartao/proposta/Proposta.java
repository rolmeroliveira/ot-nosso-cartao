package com.zup.nossocartao.proposta;

import com.zup.nossocartao.validacao.CpfOuCnpjValido;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


@Entity
public class Proposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Documento do solicitante deve ser obrigatório e válido
    @NotBlank
    @CpfOuCnpjValido
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

    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
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
}
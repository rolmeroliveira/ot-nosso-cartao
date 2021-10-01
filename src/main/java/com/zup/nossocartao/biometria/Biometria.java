package com.zup.nossocartao.biometria;


import com.zup.nossocartao.cartao.Cartao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Entity
public class Biometria {

    @Id
    private String id = UUID.randomUUID().toString();

    @NotBlank
    private String fingerPrint;
    private LocalDateTime registroEm = LocalDateTime.now();

    @NotNull
    @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String fingerPrint, Cartao cartao) {
        this.fingerPrint = Base64.getEncoder().encodeToString(fingerPrint.getBytes());
        this.cartao = cartao;
    }

    public String getId() {
        return id;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public LocalDateTime getRegistroEm() {
        return registroEm;
    }

    public Cartao getCartao() {
        return cartao;
    }
}

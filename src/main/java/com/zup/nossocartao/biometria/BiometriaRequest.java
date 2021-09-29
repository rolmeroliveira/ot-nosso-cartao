package com.zup.nossocartao.biometria;
import com.zup.nossocartao.proposta.cartao.Cartao;

import javax.validation.constraints.NotBlank;

public class BiometriaRequest {

    @NotBlank
    private String fingerPrint;

    public BiometriaRequest() {
    }

    public BiometriaRequest(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public Biometria converteParaModelBiometria(Cartao cartao) {
        return new Biometria(this.fingerPrint, cartao);
    }
    public String getFingerPrint() {
        return fingerPrint;
    }
}

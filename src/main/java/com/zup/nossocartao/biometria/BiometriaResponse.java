package com.zup.nossocartao.biometria;

import java.time.LocalDateTime;

public class BiometriaResponse {

    private LocalDateTime registroEm;

    public BiometriaResponse(Biometria biometria) {
        this.registroEm = biometria.getRegistroEm();
    }

    public LocalDateTime getRegistroEm() {
        return registroEm;
    }

}

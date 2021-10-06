package com.zup.nossocartao.avisoviagem;

public class AvisoViagemAvisoRequest {

    //Quaquer string
    private String destino;
    //formato  "yyyy-MM-dd"
    private String validoAte;

    public AvisoViagemAvisoRequest() {
    }

    public AvisoViagemAvisoRequest(String destino, String validoAte) {
        this.destino = destino;
        this.validoAte = validoAte;
    }

    public String getDestino() {
        return destino;
    }

    public String getValidoAte() {
        return validoAte;
    }
}

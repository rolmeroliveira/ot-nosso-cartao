package com.zup.nossocartao.utilidades;

import javax.servlet.http.HttpServletRequest;

public class HttpUtilidades {

    public static String itemDaRequest(HttpServletRequest request, ItemDaRequest itenDaRequest){
        String retorno = null;

        switch(itenDaRequest){
            case IP_ORIGEM:
                retorno = request.getRemoteAddr();
                break;
            case USER_AGENT:
                retorno = request.getHeader("User-Agent").toUpperCase();
                break;
            default:
                retorno = "";
        }

        return retorno;
    }



}

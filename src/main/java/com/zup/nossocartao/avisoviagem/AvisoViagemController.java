package com.zup.nossocartao.avisoviagem;

import com.zup.nossocartao.cartao.Cartao;
import com.zup.nossocartao.repository.AvisoViagemRepository;
import com.zup.nossocartao.repository.CartaoRepository;
import com.zup.nossocartao.utilidades.HttpUtilidades;
import com.zup.nossocartao.utilidades.ItemDaRequest;
import com.zup.nossocartao.validacao.CustomBusinesException;
import com.zup.nossocartao.validacao.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/avisos-viagem")
public class AvisoViagemController {

    @Autowired
    AvisoViagemRepository avisoViagemRepository;
    @Autowired
    CartaoRepository cartaoRepository;

    @PostMapping(path = "/{numeroCartao}")
    public ResponseEntity<AvisoViagemResponse> criar(@PathVariable String numeroCartao, @RequestBody AvisoViagemRequest avisoViagemRequest,  HttpServletRequest request){
        String ipClient = HttpUtilidades.itemDaRequest(request, ItemDaRequest.IP_ORIGEM);
        String user_agent = HttpUtilidades.itemDaRequest(request, ItemDaRequest.USER_AGENT);
        String idCartao = numeroCartao; //avisoViagemRequest.getIdentificadorCartao();
        Cartao cartao = cartaoRepository.findByNumero(idCartao).orElseThrow(() ->
                new CustomNotFoundException("cartao", "Este catao não existe no sistema"));

        testaFormatoData(avisoViagemRequest);

        AvisoViagem avisoViagem = avisoViagemRequest.toModel(cartao, ipClient, user_agent);
        cartao.addAvisoViagem(avisoViagem);
        cartaoRepository.save(cartao);
        AvisoViagemResponse avisoViagemResponse = new AvisoViagemResponse(avisoViagem);
        return ResponseEntity.status(HttpStatus.CREATED).body(avisoViagemResponse);
    }


    public boolean testaFormatoData(AvisoViagemRequest avisoViagemRequest) {
        String textoData = avisoViagemRequest.getDataTerminoViagem();
        LocalDate localDate = null;
        try {
            localDate = LocalDate.parse(textoData, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException  e) {
            throw  new CustomBusinesException("Data do Fim da viagem", "Por favor, Forneça um valor no formato dia/mès/ano. Dia e mes com dois dígitos e ano com quatro digitos");
        }
        return true;
    }




}

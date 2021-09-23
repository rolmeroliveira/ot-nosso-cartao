package com.zup.nossocartao.proposta;

import com.zup.nossocartao.repository.PropostaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    PropostaRepository propostaRepository;

    @PostMapping
    public ResponseEntity<PropostaResponse> adiconaOpiniao(@RequestBody @Valid PropostaRequest propostaRequest){
        Proposta propostaModelo = propostaRequest.toModel();
        Proposta propostaSalva = propostaRepository.save(propostaModelo);
        PropostaResponse propostaResponse = new PropostaResponse(propostaSalva);
        return ResponseEntity.created(URI.create("/propostas")).body(propostaResponse);
    }

}

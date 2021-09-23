package com.zup.nossocartao.proposta;

import com.zup.nossocartao.repository.PropostaRepository;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    PropostaRepository propostaRepository;

    @PostMapping
    public ResponseEntity<PropostaResponse> adiconaOpiniao(@RequestBody @Valid PropostaRequest propostaRequest) throws SQLException {
        Proposta propostaModelo = propostaRequest.toModel();


        Proposta propostaRepetida = propostaRepository.findByDocumento(propostaRequest.getDocumento());
        if (propostaRepetida != null){
            throw new SQLIntegrityConstraintViolationException("Já existe uma proposta para este documento");
        }

        Proposta propostaSalva = propostaRepository.save(propostaModelo);
        PropostaResponse propostaResponse = new PropostaResponse(propostaSalva);
        return ResponseEntity.created(URI.create("/propostas")).body(propostaResponse);
    }

}

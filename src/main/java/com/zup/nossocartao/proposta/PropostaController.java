package com.zup.nossocartao.proposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.nossocartao.proposta.analise.AnalisePropostaClient;
import com.zup.nossocartao.proposta.analise.AnaliseRequest;
import com.zup.nossocartao.proposta.analise.AnaliseResponse;
import com.zup.nossocartao.proposta.cartao.StatusAssociaCartao;
import com.zup.nossocartao.repository.PropostaRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.net.URI;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


@RestController
@RequestMapping("/propostas")
public class PropostaController {

    @Autowired
    PropostaRepository propostaRepository;
    @Autowired
    AnalisePropostaClient analisePropostaClient;

    @GetMapping(path ="/{criterio}")
    public ResponseEntity<PropostaResponse> consultarProposta(@PathVariable  String criterio){

        PropostaResponse propostaResponse;
        Proposta propostaModel;

        if (criterio.length() > 10) {
            //enviou um critério com muitos caracteres - vai buscar por documento
            propostaModel = propostaRepository.findByDocumento(criterio);
        } else {
            //Vai tentar localizar por id
            Long id = Long.parseLong(criterio);
            propostaModel = propostaRepository.getById(id);
        }

        try{
            propostaResponse  = new PropostaResponse(propostaModel);
            return ResponseEntity.status(HttpStatus.OK).body(propostaResponse);
        }catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }


    @PostMapping
    public ResponseEntity<PropostaResponse> adiconaOpiniao(@RequestBody @Valid PropostaRequest propostaRequest) throws SQLException, JsonProcessingException {
        //começa achando que vai dar tudo certo, mais adiante, a gente verifica se isso permanece...
        StatusAssociaCartao statusAssociaCartao = StatusAssociaCartao.ELEGIVEL;

        //como o serviço que verifica a restrição não leva em conta o id da proposta, decidi
        //enviar antes da persistência, para não ter que fazer roolback. Só persisto se correr tudo bemn
        //com a anãlise financeira
        boolean existeRestricao = temRestricaoFinanceira(propostaRequest);

        //aproveito o resultado da análise financeira, para lançar um erro padrão já tratado
        //e interceptável pelo handler
        if (existeRestricao){
            statusAssociaCartao = StatusAssociaCartao.NAO_ELEGIVEL;
        }
        Proposta propostaModelo = propostaRequest.toModel(statusAssociaCartao);

        //Agora verifico se a psoposta já eixste, caso afirmativo, não permito cadastrar de novo
        Proposta propostaRepetida = propostaRepository.findByDocumento(propostaRequest.getDocumento());
        if (propostaRepetida != null){
            throw new SQLIntegrityConstraintViolationException("Já existe uma proposta para este documento");
        }

        Proposta propostaSalva = propostaRepository.save(propostaModelo);
        PropostaResponse propostaResponse = new PropostaResponse(propostaSalva);
        return ResponseEntity.created(URI.create("/propostas")).body(propostaResponse);
    }



    //Este método usa o feign client para retornar um objeto que indica se a proposta tem ou não
    //restriçoes financeiras. A lógica do serviço consultado está hardcoded, definindo que se o documento
    //passado começa com 3, tem restrição, caso contrário, não tem restrição
    private boolean temRestricaoFinanceira(PropostaRequest propostaRequest) throws JsonProcessingException {
        AnaliseRequest analiseRequest = new AnaliseRequest(propostaRequest);
        AnaliseResponse analiseResponse;


        try {
            analiseResponse = analisePropostaClient.analisar(analiseRequest);
        }catch (FeignException e){
            ObjectMapper objectMapper = new ObjectMapper();
            analiseResponse = objectMapper.readValue(e.contentUTF8(), AnaliseResponse.class);
        }
        analiseResponse  = analiseResponse;

        if (analiseResponse.getResultadoSolicitacao().equals("SEM_RESTRICAO")){
            return false;
        }else{
            return true;
        }
    }





}

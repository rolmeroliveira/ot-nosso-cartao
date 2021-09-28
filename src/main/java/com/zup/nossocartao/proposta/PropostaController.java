package com.zup.nossocartao.proposta;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.nossocartao.proposta.analise.AnalisePropostaClient;
import com.zup.nossocartao.proposta.analise.AnaliseRequest;
import com.zup.nossocartao.proposta.analise.AnaliseResponse;
import com.zup.nossocartao.proposta.cartao.StatusAssociaCartao;
import com.zup.nossocartao.repository.PropostaRepository;
import feign.FeignException;
import org.hibernate.dialect.function.AbstractAnsiTrimEmulationFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

        //Aqui poderia ser incluída uma lógica que limitasse a consulta ao próprio proponente, ou a um
        //Agente ou serviço autorizado a consultar propostas de terceiros (tipo um gerente de banco)

        PropostaResponse propostaResponse;
        Proposta propostaModel;

        if (criterio.length() > 10) {
            //enviou um critério com muitos caracteres - vai buscar por documento
            String documento = criterio;
            propostaModel = propostaRepository.findByDocumento(criterio);
        } else {
            //Vai tentar localizar por id, mas pode ter enviado uma sequencia curta, que não pode ser convertida em número
            //Dava pra fazer mais sofisticado, enviando uma msg amigável pro cliente, usando um handler, mas não houve tempo
            try{
                Long id = Long.parseLong(criterio);
                propostaModel = propostaRepository.getById(id);
            }catch(NumberFormatException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        }

        try{
            propostaResponse  = new PropostaResponse(propostaModel);
            return ResponseEntity.status(HttpStatus.OK).body(propostaResponse);
        }catch(NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }


    @PostMapping
    public ResponseEntity<String> adiconarProposta(@RequestBody @Valid PropostaRequest propostaRequest) throws SQLException, JsonProcessingException {

        StatusAssociaCartao statusAssociaCartao =  varificarStatusProposta(propostaRequest);
        boolean propostaRepetida = isPropostaRepetida(propostaRequest);

        //Pode ter dado alguma falha na conexao do feigin...
        if (statusAssociaCartao == StatusAssociaCartao.NAO_VERIFICADA){
            //Não vou inserir uma proposta sem estar completa
            throw new SQLIntegrityConstraintViolationException("Falha na verificação dos dados. Tente mais tarde");
        }

        //a proposta já pode ter sido inserida antes
        if (propostaRepetida){
            //Repetida não vale!
            throw new SQLIntegrityConstraintViolationException("Já existe uma proposta para este documento");
        }

        //crio um objeto do modelo pra persistir
        Proposta propostaModelo = propostaRequest.toModel(statusAssociaCartao);
        //persisto em uma instrução separada. Vamos pensar em quem vai ler...
        Proposta propostaSalva = propostaRepository.save(propostaModelo);

        return ResponseEntity.status(HttpStatus.CREATED).body("Poposta inserida com êxito");
    }

    //só pra isolar esta checagem
    private boolean isPropostaRepetida(PropostaRequest propostaRequest) {
        Proposta propostaRepetida = propostaRepository.findByDocumento(propostaRequest.getDocumento());
        return (propostaRepetida != null);
    }

    //Retorna três possíveis status: Elegivel, Nao elegivel, ou o feign não conseguiu checar
    private StatusAssociaCartao varificarStatusProposta(PropostaRequest propostaRequest)  {
        AnaliseRequest analiseRequest = new AnaliseRequest(propostaRequest);
        AnaliseResponse analiseResponse;

        //o feign falho em conectar com o banco. Deu algo errado fora da minha api
        try {
            analiseResponse = analisePropostaClient.analisar(analiseRequest);
        }catch (FeignException e){
            return StatusAssociaCartao.NAO_VERIFICADA;
        }

        if (analiseResponse.getResultadoSolicitacao().equals("SEM_RESTRICAO")){
            return StatusAssociaCartao.NAO_ELEGIVEL;
        }else{
            return StatusAssociaCartao.ELEGIVEL;
        }
    }





}

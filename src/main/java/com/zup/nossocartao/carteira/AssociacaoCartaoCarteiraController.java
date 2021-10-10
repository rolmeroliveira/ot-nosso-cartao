package com.zup.nossocartao.carteira;

import com.zup.nossocartao.cartao.Cartao;
import com.zup.nossocartao.proposta.Proposta;
import com.zup.nossocartao.repository.AssociaCartaoCarteiraRepository;
import com.zup.nossocartao.repository.CartaoRepository;
import com.zup.nossocartao.repository.PropostaRepository;
import com.zup.nossocartao.utilidades.HttpUtilidades;
import com.zup.nossocartao.utilidades.ItemDaRequest;
import com.zup.nossocartao.validacao.CustomBusinesException;
import com.zup.nossocartao.validacao.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@RequestMapping("/associacao")
public class AssociacaoCartaoCarteiraController {

    @Autowired
    AssociaCartaoCarteiraRepository associaCartaoCarteiraRepository;
    @Autowired
    CartaoRepository cartaoRepository;
    @Autowired
    PropostaRepository propostaRepository;
    @Autowired
    AssociacaoCartaoCarteiraClient associacaoCartaoCarteiraClient;


    @PostMapping(path = "/{numeroCartao}")
    public ResponseEntity<String> associar(@PathVariable String numeroCartao,
                                           @RequestBody @Valid AssociacaoCartaoCarteiraRequest associacaoCartaoCarteiraRequest,
                                           HttpServletRequest request){

        //verifica se o cartão existe
        Cartao cartao = cartaoRepository.findByNumero(numeroCartao).orElseThrow(() ->
                new CustomNotFoundException("cartao", "Este catao não existe no sistema"));

        String ipClient = HttpUtilidades.itemDaRequest(request, ItemDaRequest.IP_ORIGEM);
        String user_agent = HttpUtilidades.itemDaRequest(request, ItemDaRequest.USER_AGENT);
        String emailRecebidoNoRequest = associacaoCartaoCarteiraRequest.getEmail();
        String carteiraRecebidaNoRequest = associacaoCartaoCarteiraRequest.getCarteira();

        //Se o cartão já for associado a alguma carteira, não prosseguir
        verificaSeCartaoJaEstaAssociado(cartao);

        //Verifica se o email informado é do cartao
        verificaSeEmailInformadoPertenceAoCartao(cartao,  emailRecebidoNoRequest);

        //tenta fazer a vinculação no sistema bancário
        associaNoSistemaBancario(numeroCartao, carteiraRecebidaNoRequest,emailRecebidoNoRequest);

        //cria um objeto que representa a associacao do cartao com a careira desejada
        AssociacaoCartaoCarteira associacaoCartaoCarteira = associacaoCartaoCarteiraRequest.toModel(cartao, ipClient, user_agent);

        //define o vinculo entre o objeto cartao e o objeto associacao, criado acima
        cartao.associarCartaoAUmaCarteira (associacaoCartaoCarteira);

        //Persiste o cartao para persistir, por cascata, a associacao com a carteira
        cartaoRepository.save(cartao);

        return ResponseEntity.ok("Associacao ocorreu com sucesso");

    }

    //Checa se o cartão enviado no request de associacao é o mesmo que consta da proposta do catao
    private void verificaSeEmailInformadoPertenceAoCartao(Cartao cartao, String email){
        Long idProposta = cartao.getProposta().getId();
        Proposta proposta = propostaRepository.findById(idProposta).orElseThrow(() ->
                new CustomNotFoundException("Proposta", "O email vinculado a este catao nao foi encontrado"));
        String emailProposta = proposta.getEmail();

        //Se o email recebido na rquest é o mesmo da proposta, só sai do método e pronto, senão lança uma exception
        if (email.equals(emailProposta)){
            return;
        }else{
            throw new CustomBusinesException("email", "o email informaod para associacao nao e igual ao da proposta do cartao");
        }
    }

    //Solicita associação do cartão ao sistema bancário - API de terceiros
    private void associaNoSistemaBancario (String numeroCartao, String carteira, String email){
        AssociaCartaoCarteiraBancoRequest accbReq = new AssociaCartaoCarteiraBancoRequest(email, carteira);
        AssociaCartaoCarteiraBancoResponse accbResp =  associacaoCartaoCarteiraClient.requisitaAssociacaoSistemaBancario(numeroCartao, accbReq);
        String resultadoAssociacao = accbResp.getResultado();
        //Se associou no sistema bancario, sai do metodo e fim. Senão lanca uma exception
        if (resultadoAssociacao.equals("ASSOCIADA")){
            return;
        }else{
            throw new CustomBusinesException(null, "Não foi possível solicitar a associacao ao sistema bancario") ;
        }
    }

    private void verificaSeCartaoJaEstaAssociado(Cartao cartao){
        if(associaCartaoCarteiraRepository.findByCartao(cartao).isPresent()){
            throw new CustomBusinesException("Cartao", "Cartao ja esta associado a uma carteira");
        }
    }

}

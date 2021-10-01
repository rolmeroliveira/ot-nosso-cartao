package com.zup.nossocartao.bloqueios;


import com.zup.nossocartao.cartao.Cartao;
import com.zup.nossocartao.proposta.analise.AnalisePropostaClient;
import com.zup.nossocartao.repository.CartaoRepository;
import com.zup.nossocartao.validacao.CustomBusinesException;
import com.zup.nossocartao.validacao.CustomNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bloqueios")
public class BloqueioCartaoController {

    @Autowired
    CartaoRepository cartaoRepository;

    @Autowired
    BloqueioCartaoAvisoClient bloqueioCartaoAvisoClient;

    @PostMapping
    public ResponseEntity<String> bloquearCartao(@RequestBody BloqueioCartaoRequest bloqueioCartaoRequest, HttpServletRequest request,  @AuthenticationPrincipal Jwt jwt){
        Long idCartao = bloqueioCartaoRequest.getIdCartao();

        Cartao cartaoParaBloquear = cartaoRepository.findById(idCartao).orElseThrow(() ->
                new CustomNotFoundException("cartao", "Este catao não existe no sistema"));

        //o número bem grandao - não o id - usado para informar ao sistema legado
        String numeroCartao = cartaoParaBloquear.getNumero();

        String documentoUsuarioLogado = (String) jwt.getClaims().get("cpf");
        String ipOrigemReqisicao = request.getRemoteAddr();
        String userAgentRequisicao = request.getHeader(HttpHeaders.USER_AGENT);

        var cartaoPertenceAoUsuarioLogado = cartaoParaBloquear.getProposta().getDocumento().equals(documentoUsuarioLogado);

        if (!cartaoPertenceAoUsuarioLogado) {
            throw new CustomBusinesException("cartao", "O suáiro logado não é o titular do cartao");
        }

        if (cartaoParaBloquear.isBloqeuado()) {
            throw new CustomBusinesException("cartao", "Já existe um bloqueio ativo para este cartão");
        }

        if (ipOrigemReqisicao.isEmpty() || userAgentRequisicao.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        //Verifica se foi possivel avisaro ao sistema legado sobro o bloqueio que estamos tentando fazer
        //Se o aviso não foi bem sucedido, cai fora, antes de gravar no banco lodal
        boolean conseguiuAvisarSistemaLegado =  avisaSistemaLegado(numeroCartao);

        if(conseguiuAvisarSistemaLegado) {
            BloqueioCartao bloqueioCartao = new BloqueioCartao(cartaoParaBloquear, ipOrigemReqisicao, userAgentRequisicao);
            cartaoParaBloquear.addBloqueioCartao(bloqueioCartao);
            cartaoRepository.save(cartaoParaBloquear);
            return ResponseEntity.status(HttpStatus.OK).body("Foi incluido um bloqueio para o cartal. ");
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha na propagação das informações. Bloqueio não efetuado ");
        }

    }


    private boolean avisaSistemaLegado (String numeroCartao){
        BloqueioCartaoAvisoRequest bloqueioCartaoAvisoRequest = new BloqueioCartaoAvisoRequest("nosso cartao");
        BloqueioCartaoAvisoResponse bloqueioCartaoAvisoResponse =  bloqueioCartaoAvisoClient.avisarSistemalegado(
                numeroCartao, bloqueioCartaoAvisoRequest);
                String resuiltadoAviso = bloqueioCartaoAvisoResponse.getResultado();
                if (resuiltadoAviso.equals("BLOQUEADO")){
                    return true;
                }else{
                    return false;
                }
    }


}

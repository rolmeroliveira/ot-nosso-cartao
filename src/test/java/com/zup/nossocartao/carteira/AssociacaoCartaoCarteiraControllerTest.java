package com.zup.nossocartao.carteira;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.nossocartao.cartao.*;
import com.zup.nossocartao.proposta.Proposta;
import com.zup.nossocartao.proposta.PropostaRequest;
import com.zup.nossocartao.repository.AssociaCartaoCarteiraRepository;
import com.zup.nossocartao.repository.CartaoRepository;
import com.zup.nossocartao.repository.PropostaRepository;
import com.zup.nossocartao.validacao.CustomBusinesException;
import net.bytebuddy.description.type.TypeList;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.constraints.AssertTrue;
import java.lang.module.ResolutionException;
import java.math.BigDecimal;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class AssociacaoCartaoCarteiraControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private PropostaRepository propostaRepository;
    @Autowired
    private CartaoRepository cartaoRepository;
    @Autowired
    private AssociaCartaoCarteiraRepository associacaoRepository;
    @Autowired
    private VinculaCartaoClient vinculaCartaoClient;


    @Test
    @DisplayName("deve vincular um cartao a uma carteira com sucesso")
    void deveVincularUmCartaoAUmaCarteiraComSucesso() throws Exception {

    //cria uma proposta nova
    Proposta p = new Proposta("795.658.760-30", "email@email.com", "um nome", "Um denreco", new BigDecimal(1000.0), StatusAssociaCartao.ELEGIVEL);
    propostaRepository.save(p);
    Optional<Proposta> ps =  propostaRepository.findById(p.getId());
    if (ps.isPresent()){
        //obtem um cartao para a proposta
        //em produção isso ocorre como uma tarefa agendada, mas o teste não pode esperar...
        VinculaCartaoRequest vinculaCartaoRequest = new VinculaCartaoRequest(p);
        vinculaCartaoClient.associaCartao(vinculaCartaoRequest);
    }

    //recupera o cartão da proposta
    Cartao cartao = cartaoRepository.findByProposta_Id(p.getId());

    AssociaCartaoCarteiraBancoRequest accbr = new AssociaCartaoCarteiraBancoRequest("email@email.com","Paypal");

    //agora vai vincular o cartao a uma carteira (paypal)
    MockHttpServletRequestBuilder request = post("/associacao/" + cartao.getNumero())
            .header("User-Agent", "Minha API")

            .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(accbr));

        mvc.perform(request)
            .andExpect(status().isOk());
    }



    @Test
    @DisplayName("deve rejeitar a asssociacao de cartao a uma carteira inexistente")
    void deveRejeitarAsssociacaoDeCartaoComCarteiraInexistente() throws Exception {

        String emailTeste = "outroemail@email.com";

        //cria uma proposta nova
        Proposta p = new Proposta("489.218.490-09", emailTeste, "outro nome", "Outro denreco", new BigDecimal(1000.0), StatusAssociaCartao.ELEGIVEL);
        propostaRepository.save(p);
        Optional<Proposta> ps =  propostaRepository.findById(p.getId());
        if (ps.isPresent()){
            //obtem um cartao para a proposta
            //em produção isso ocorre como uma tarefa agendada, mas o teste não pode esperar...
            VinculaCartaoRequest vinculaCartaoRequest = new VinculaCartaoRequest(p);
            vinculaCartaoClient.associaCartao(vinculaCartaoRequest);
        }

        //recupera o cartão da proposta
        Cartao cartao = cartaoRepository.findByProposta_Id(p.getId());

        AssociaCartaoCarteiraBancoRequest accbr = new AssociaCartaoCarteiraBancoRequest(emailTeste,"Inexistente");

        //agora vai vincular o cartao a uma carteira (paypal)
        MockHttpServletRequestBuilder request = post("/associacao/" + cartao.getNumero())
                .header("User-Agent", "Minha API")

                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(accbr));

        mvc.perform(request)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof CustomBusinesException));

    }



}
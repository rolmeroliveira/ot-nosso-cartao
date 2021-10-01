package com.zup.nossocartao.proposta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.nossocartao.cartao.Cartao;
import com.zup.nossocartao.repository.CartaoRepository;
import com.zup.nossocartao.repository.PropostaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
//@Transactional
class PropostaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PropostaRepository propostaRepository;
    @Autowired
    private CartaoRepository cartaoRepository;


    //O cpf usado para teste, foi dinamicamente gerado por um serviço gerador de cpf
    //o ducomento não corresponde a um cadastro real de contribuinte, portanto, não expõe
    //dados pessoais
    @Test
    @DisplayName("deve cadastrar uma proposta com sucesso")
    void deveCadastrarUmaPropostaComSucesso() throws Exception {
        limpaDadosDeTeste();
        PropostaRequest propostaRequest = gerarPropostaRequestParaTeste();

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isCreated());

    }


    @Test
    @DisplayName("deve rejeitar uma proposta com cpf invalido")
    void deveRejeitarUmaPropostaComCpfInvalido() throws Exception{
        PropostaRequest propostaRequest = gerarPropostaRequestCpfInvalidoParaTeste();

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("documento"));
    }


    @Test
    @DisplayName("deve rejeitar uma proposta com cpf em branco")
    void deveRejeitarUmaPropostaComCpfEmBranco() throws Exception{
        PropostaRequest propostaRequest = gerarPropostaRequestCpfEmBrancoParaTeste();

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0]field").value("documento"))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));


    }

    @Test
    @DisplayName("deve rejeitar uma proposta com endereco em branco")
    void deveRejeitarUmaPropostaComEnderecoEmBranco() throws Exception {
        PropostaRequest propostaRequest = gerarPropostaRequestEnderecoEmBrancoParaTeste();

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("endereco"))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));

    }

    @Test
    @DisplayName("deve rejeitar uma proposta com salario zerado")
    void deveRejeitarUmaPropostaComSalarioZerado() throws Exception {
        PropostaRequest propostaRequest = gerarPropostaRequestSalarioZeradoParaTeste();

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("salario"))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));

    }


    @Test
    @DisplayName("deve rejeitar uma proposta com salario negativo")
    void deveRejeitarUmaPropostaComSalarionegativo() throws Exception {
        PropostaRequest propostaRequest = gerarPropostaRequestSalarioNegativoParaTeste();

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("salario"))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));

    }




    @Test
    @DisplayName("deve rejeitar uma proposta com documento repetido")
    void deveRejeitarUmaPropostaComDocumentoRepetido() throws Exception {
        PropostaRequest propostaRequest1 = gerarPropostaRequestParaTeste();

        //inserre a primeira vez. iss não dever gerar 422
        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest1));
        mvc.perform(request);

        //Insere de novo o mesmo registro. Aqui o 422 tem que pipocar!
        MockHttpServletRequestBuilder request2 = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest1));

        mvc.perform(request2)
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.statusCode").value("422"))
                .andExpect(result -> Assertions.assertTrue(result.getResolvedException()
                        instanceof SQLIntegrityConstraintViolationException));

        limpaDadosDeTeste();

    }


    @Test
    @DisplayName("deve rejeitar uma busca por id com criterio nao numerico")
    void deveRejeitarUmaBuscaPorIdComCriterioNaoNumerico() throws Exception {

        //inserre a primeira vez. iss não dever gerar 422
        MockHttpServletRequestBuilder request = get("/propostas/aa")
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isBadRequest());
    }



    @Test
    void limpaDadosDeTeste() {
        //encontro a proposta fake usada pra teste
        Proposta p = propostaRepository.findByDocumento("795.658.760-30");
        //encontro o cartao vinculado á proposta (se proposta é de teste, cartao também é)
        Cartao c = null;
        if (p != null) {
            c = cartaoRepository.findByProposta_Id(p.getId());
        }
        //deleto ambos, pra não impedir o test de criação
        if (c != null) cartaoRepository.deleteById(c.getId());
        if (p != null) propostaRepository.deleteById(p.getId());
    }

    private PropostaRequest gerarPropostaRequestParaTeste(){
        //Crio uma porposta somente para teste
        PropostaRequest p = new PropostaRequest(
                "795.658.760-30",
                "umemail@email.com",
                "un nome de alguem",
                "edereco de alguem",
                new BigDecimal(20));
        return p;
    }

    private PropostaRequest gerarPropostaRequestCpfInvalidoParaTeste(){
        //Crio uma porposta somente para teste
        PropostaRequest p = new PropostaRequest(
                "795.658.760-00",
                "umemail@email.com",
                "un nome de alguem",
                "edereco de alguem",
                new BigDecimal(20));
        return p;
    }

    private PropostaRequest gerarPropostaRequestCpfEmBrancoParaTeste(){
        //Crio uma porposta somente para teste
        PropostaRequest p = new PropostaRequest(
                "",
                "umemail@email.com",
                "un nome de alguem",
                "edereco de alguem",
                new BigDecimal(20));
        return p;
    }

    private PropostaRequest gerarPropostaRequestEnderecoEmBrancoParaTeste(){
        //Crio uma porposta somente para teste
        PropostaRequest p = new PropostaRequest(
                "795.658.760-30",
                "umemail@email.com",
                "un nome de alguem",
                "",
                new BigDecimal(20));
        return p;
    }

    private PropostaRequest gerarPropostaRequestSalarioZeradoParaTeste(){
        //Crio uma porposta somente para teste
        PropostaRequest p = new PropostaRequest(
                "795.658.760-30",
                "umemail@email.com",
                "un nome de alguem",
                "Algum endereco",
                new BigDecimal(0));
        return p;
    }

    private PropostaRequest gerarPropostaRequestSalarioNegativoParaTeste(){
        //Crio uma porposta somente para teste
        PropostaRequest p = new PropostaRequest(
                "795.658.760-30",
                "umemail@email.com",
                "un nome de alguem",
                "Algum endereco",
                new BigDecimal(-0.1));
        return p;
    }
}
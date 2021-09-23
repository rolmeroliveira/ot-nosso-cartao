package com.zup.nossocartao.proposta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.nossocartao.repository.PropostaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class PropostaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PropostaRepository propostaRepository;


    //O cpf usado para teste, foi dinamicamente gerado por um serviço gerador de cpf
    //o ducomento não corresponde a um cadastro real de contribuinte, portanto, não expõe
    //dados pessoais
    @Test
    @DisplayName("deve cadastrar uma proposta com sucesso")
    void deveCadastrarUmaPropostaComSucesso() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest();
        propostaRequest.setDocumento("063.886.880-71");
        propostaRequest.setEndereco("Um endereço qualquer");
        propostaRequest.setEmail("umemail@email.com");
        propostaRequest.setNome("nome de uma pessoa");
        propostaRequest.setSalario(new BigDecimal(3200.44));

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isCreated());

    }


    @Test
    @DisplayName("deve rejeitar uma proposta com cpf invalido")
    void deveRejeitarUmaPropostaComCpfInvalido() throws Exception{
        PropostaRequest propostaRequest = new PropostaRequest();
        propostaRequest.setDocumento("770.385.507-10");
        propostaRequest.setEndereco("Um endereço qualquer");
        propostaRequest.setEmail("umemail@email.com");
        propostaRequest.setNome("nome de uma pessoa");
        propostaRequest.setSalario(new BigDecimal(3200.44));

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("documento"))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));
    }


    @Test
    @DisplayName("deve rejeitar uma proposta com cpf em branco")
    void deveRejeitarUmaPropostaComCpfEmBranco() throws Exception{
        PropostaRequest propostaRequest = new PropostaRequest();
        propostaRequest.setDocumento("");
        propostaRequest.setEndereco("Um endereço qualquer");
        propostaRequest.setEmail("umemail@email.com");
        propostaRequest.setNome("nome de uma pessoa");
        propostaRequest.setSalario(new BigDecimal(3200.44));

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0]field").value("documento"))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));


    }

    @Test
    @DisplayName("deve rejeitar uma proposta com endereco em branco")
    void deveRejeitarUmaPropostaComEnderecoEmBranco() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest();
        propostaRequest.setDocumento("187.353.970-39");
        propostaRequest.setEndereco("");
        propostaRequest.setEmail("umemail@email.com");
        propostaRequest.setNome("nome de uma pessoa");
        propostaRequest.setSalario(new BigDecimal(3200.44));

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("endereco"))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));

    }

    @Test
    @DisplayName("deve rejeitar uma proposta com salario zerado")
    void deveRejeitarUmaPropostaComSalarioZerado() throws Exception {
        PropostaRequest propostaRequest = new PropostaRequest();
        propostaRequest.setDocumento("187.353.970-39");
        propostaRequest.setEndereco("Um endereco valido");
        propostaRequest.setEmail("umemail@email.com");
        propostaRequest.setNome("nome de uma pessoa");
        propostaRequest.setSalario(new BigDecimal(0.0));

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("salario"))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));

    }


    @Test
    @DisplayName("deve rejeitar uma proposta com salario negativo")
    void deveRejeitarUmaPropostaComSalarionegativo() throws Exception {
        PropostaRequest propostaRequest = propostaSalarioNegativo();

        MockHttpServletRequestBuilder request = post("/propostas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(propostaRequest));

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("salario"))
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof org.springframework.web.bind.MethodArgumentNotValidException));

    }




    @Test
    @DisplayName("deve rejeitar uma proposta com documento repetido")
    void deveRejeitarUmaPropostaComDocumentoRepetido() throws Exception {
        PropostaRequest propostaRequest1 = propostaOK();

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
                .andExpect(result -> assertTrue(result.getResolvedException()
                        instanceof SQLIntegrityConstraintViolationException));


    }


    private PropostaRequest propostaOK(){
        PropostaRequest propostaRequest = new PropostaRequest();
        propostaRequest.setDocumento("187.353.970-39");
        propostaRequest.setEndereco("Um endereco valido");
        propostaRequest.setEmail("umemail@email.com");
        propostaRequest.setNome("nome de uma pessoa");
        propostaRequest.setSalario(new BigDecimal(10.0));
        return propostaRequest;
    }

    private PropostaRequest propostaSalarioNegativo(){
        PropostaRequest propostaRequest = new PropostaRequest();
        propostaRequest.setDocumento("187.353.970-39");
        propostaRequest.setEndereco("Um endereco valido");
        propostaRequest.setEmail("umemail@email.com");
        propostaRequest.setNome("nome de uma pessoa");
        propostaRequest.setSalario(new BigDecimal(-0.1));
        return propostaRequest;
    }




}
package com.zup.nossocartao.repository;

import com.zup.nossocartao.proposta.Proposta;
import com.zup.nossocartao.cartao.StatusAssociaCartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    Proposta findByDocumento(String documento);
    List<Proposta> findBystatusAndCartaoIsNull(StatusAssociaCartao statusProposta);
}

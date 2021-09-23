package com.zup.nossocartao.repository;

import com.zup.nossocartao.proposta.Proposta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropostaRepository extends JpaRepository<Proposta, Long> {

    Proposta findByDocumento(String documento);
}

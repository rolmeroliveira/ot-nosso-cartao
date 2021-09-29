package com.zup.nossocartao.repository;

import com.zup.nossocartao.proposta.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Cartao findByProposta_Id(long id);
}

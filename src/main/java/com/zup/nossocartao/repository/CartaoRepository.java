package com.zup.nossocartao.repository;

import com.zup.nossocartao.cartao.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartaoRepository extends JpaRepository<Cartao, Long> {
    Cartao findByProposta_Id(long id);
    Optional<Cartao> findByNumero(String idCartao);
}

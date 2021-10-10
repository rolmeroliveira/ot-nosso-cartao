package com.zup.nossocartao.repository;

import com.zup.nossocartao.cartao.Cartao;
import com.zup.nossocartao.carteira.AssociacaoCartaoCarteira;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssociaCartaoCarteiraRepository  extends JpaRepository<AssociacaoCartaoCarteira, Long> {

    Optional<AssociacaoCartaoCarteira> findByCartao(Cartao cartao);

}

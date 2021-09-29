package com.zup.nossocartao.biometria;


import com.zup.nossocartao.proposta.cartao.Cartao;
import com.zup.nossocartao.repository.CartaoRepository;
import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/biometrias/cartoes/{id}")
public class BiometriaController {


    @Autowired
    private BiometriaRepository biometriaRepository;

    @Autowired
    private CartaoRepository cartaoRepository;


    @PostMapping
    @Transactional
    public ResponseEntity<BiometriaResponse> inserirBiometria(@Valid @RequestBody BiometriaRequest biometriaRequest,
                                                   @PathVariable Long id,
                                                   UriComponentsBuilder uriComponentsBuilder) {

        Optional<Cartao> cartaoOptional = cartaoRepository.findById(id);
        if (cartaoOptional.isPresent()) {
            if (Base64.isBase64(biometriaRequest.getFingerPrint())) {
                Biometria biometria = biometriaRequest.converteParaModelBiometria(cartaoOptional.get());
                biometriaRepository.save(biometria);

                URI uri = uriComponentsBuilder.path("/biometrias/cartoes/{id}").buildAndExpand(biometria.getId()).toUri();
                return ResponseEntity.created(uri).body(new BiometriaResponse(biometria));
            }
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.notFound().build();
    }
}

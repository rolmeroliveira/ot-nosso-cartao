package com.zup.nossocartao.util;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;

import javax.persistence.AttributeConverter;

public class Encriptador implements AttributeConverter<String,String> {


    String salt = KeyGenerators.string().generateKey();
    String senha = "${crypto-key}";
    TextEncryptor e =  Encryptors.text(senha, salt);

    @Override
    public String convertToDatabaseColumn(String attribute) {
        String encriptado = e.encrypt(attribute);
        return encriptado;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        String decriptado = e.decrypt(dbData);
        return decriptado;
    }
}

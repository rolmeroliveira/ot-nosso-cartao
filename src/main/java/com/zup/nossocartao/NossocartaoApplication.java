package com.zup.nossocartao;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.keygen.KeyGenerators;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableScheduling
@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
public class NossocartaoApplication {
	public static void main(String[] args) {
		SpringApplication.run(NossocartaoApplication.class, args);
	}

}



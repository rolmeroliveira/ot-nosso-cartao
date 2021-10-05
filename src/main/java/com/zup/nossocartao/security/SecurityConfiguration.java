package com.zup.nossocartao.security;


import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorizeRequest ->
                        authorizeRequest
                                .antMatchers(HttpMethod.GET, "/propostas/*").hasAuthority("SCOPE_ler-proposta")
                                .antMatchers(HttpMethod.POST, "/propostas").hasAuthority("SCOPE_criar-proposta")
                                .antMatchers(HttpMethod.POST, "/bloqueios").hasAuthority("SCOPE_bloquear-cartao")
                                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
                                .antMatchers(HttpMethod.GET, "/aviso-viagem/**").permitAll()
                                //.antMatchers(HttpMethod.POST, "/**").permitAll()
                                .anyRequest().authenticated()
                )
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}

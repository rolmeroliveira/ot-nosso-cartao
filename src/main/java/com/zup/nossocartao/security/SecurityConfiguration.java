package com.zup.nossocartao.security;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    A implementacao do metodo foi comentada porque era muito chata de manter em tempo de dev
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests(authorizeRequest ->
//                        authorizeRequest
//                                .antMatchers(HttpMethod.GET, "/propostas/*").hasAuthority("SCOPE_ler-proposta")
//                                .antMatchers(HttpMethod.POST, "/propostas").hasAuthority("SCOPE_criar-proposta")
//                                .antMatchers(HttpMethod.POST, "/bloqueios").hasAuthority("SCOPE_bloquear-cartao")
//                                .antMatchers(HttpMethod.POST, "/associacao/**").hasAuthority("SCOPE_bloquear-cartao")
//                                .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
//                                .antMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
//                                .antMatchers(HttpMethod.POST, "/avisos-viagem/**").hasAuthority("SCOPE_cria-aviso-viagem")
//                                //.antMatchers(HttpMethod.POST, "/**").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
//    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable().csrf().disable().authorizeRequests()
                .anyRequest().permitAll();


    }


}

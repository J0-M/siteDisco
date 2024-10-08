package com.example.siteDiscoBackend.Infra.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())//desabilita essa configuração
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))


                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()//login e register, qualquer um pode realizar
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/registerAdmin").permitAll()
                        .requestMatchers(HttpMethod.POST, "/order").authenticated()
                        .requestMatchers(HttpMethod.POST, "/address").authenticated()
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")//todas as funções create apenas o ADMIN pode realizar

                        .requestMatchers(HttpMethod.GET, "/product").permitAll()
                        .requestMatchers(HttpMethod.GET, "/order").authenticated()
                        .requestMatchers(HttpMethod.GET, "/auth/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/address").authenticated()
                        .requestMatchers(HttpMethod.GET, "/address/user/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/auth/users").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/order/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/auth/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/address/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")//todas as funções update apenas o ADMIN pode realizar

                        .requestMatchers(HttpMethod.DELETE, "/auth/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/order/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/address/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/auth/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")//todas as funções delete apenas o ADMIN pode realizar

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

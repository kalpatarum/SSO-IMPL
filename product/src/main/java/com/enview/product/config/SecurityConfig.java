package com.enview.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Enables Spring Security's web security support
@EnableMethodSecurity // Enables method-level security (e.g., @PreAuthorize)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        //.requestMatchers("/products").hasAuthority("SCOPE_read") // Require 'products:read' scope for /api/products
                        //.requestMatchers(HttpMethod.POST,"/products/add").hasAuthority("SCOPE_write") // Require 'products:read' scope for /api/products
                        .anyRequest().authenticated() // All other requests must be authenticated
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {
                    /*jwt.jwtAuthenticationConverter(source -> {
                        System.out.println("JWT SRC:"+source.toString());
                        return new JwtAuthenticationToken(source);
                    });*/
                })) // Configure as an OAuth2 Resource Server with JWT validation
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Use stateless sessions (typical for JWT)

        return http.build();
    }
}

package com.enview.product.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        .requestMatchers("/api/products").hasAuthority("SCOPE_read") // Require 'products:read' scope for /api/products
                        .anyRequest().authenticated() // All other requests must be authenticated
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {})) // Configure as an OAuth2 Resource Server with JWT validation
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Use stateless sessions (typical for JWT)

        return http.build();
    }
}

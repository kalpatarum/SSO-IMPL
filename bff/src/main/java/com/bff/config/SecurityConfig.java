package com.bff.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {


    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/me").permitAll()
                        .pathMatchers("/", "/login**","/logout").permitAll()
                        .anyExchange().authenticated()
                )
                .csrf(csrf -> {
                    csrf.disable();
                })
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((exchange, authentication) -> {
                            // Optional: Extract ID Token to pass to IDP
                            //String idToken = ""; // if needed, extract from authentication

                            URI idpLogoutUri = URI.create("http://127.0.0.1:8080/logout?post_logout_redirect_uri=http://localhost:5173");

                            exchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
                            exchange.getExchange().getResponse().getHeaders().setLocation(idpLogoutUri);
                            return Mono.empty(); // No redirect
                        })
                )
                .oauth2Login(oauth2Login -> {
                    // You can customize login options here if needed
                    oauth2Login.authenticationSuccessHandler(
                            new RedirectServerAuthenticationSuccessHandler("http://localhost:5173")
                    );
                })
                .oauth2Client(oauth2Client -> {
                    // Enables TokenRelay (no extra config needed unless customizing)
                });
                //.logout(logoutSpec -> logoutSpec.);

        return http.build();
    }
    @Bean
    public ReactiveOAuth2UserService<OAuth2UserRequest, OAuth2User> customUserService() {
        return userRequest -> {
            String accessToken = userRequest.getAccessToken().getTokenValue();
            try {
                SignedJWT jwt = SignedJWT.parse(accessToken);
                JWTClaimsSet claims = jwt.getJWTClaimsSet();
                String sub = claims.getSubject();

                return Mono.just(new DefaultOAuth2User(
                        List.of(new SimpleGrantedAuthority("ROLE_USER")),
                        Map.of("sub", sub),
                        "sub"
                ));
            } catch (ParseException e) {
                return Mono.error(new RuntimeException("Invalid JWT", e));
            }
        };
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:5173,http://localhost:3000"); // React UI
        corsConfig.addAllowedMethod("OPTIONS");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");// GET, POST, etc.
        corsConfig.addAllowedHeader("*");
        corsConfig.setAllowCredentials(true); // If you're using cookies/session

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }

}

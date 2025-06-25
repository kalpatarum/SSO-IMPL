package com.bff.config;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

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
                        .pathMatchers("/", "/login**").permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2Login(oauth2Login -> {
                    // You can customize login options here if needed
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

    /*@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login**").permitAll()
                        .anyRequest().authenticated()
                )
                //.oauth2Login(withDefaults()); // modern, recommended style
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(this.customOAuth2UserService())
                        )
                );
        return http.build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return userRequest -> {
            String accessToken = userRequest.getAccessToken().getTokenValue();

            // Decode JWT manually with Nimbus
            String sub;
            try {
                SignedJWT signedJWT = SignedJWT.parse(accessToken);
                JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
                sub = claimsSet.getSubject();
            } catch (ParseException e) {
                throw new RuntimeException("Invalid JWT format", e);
            }

            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    Collections.singletonMap("sub", sub),
                    "sub"
            );
        };
    }*/
}

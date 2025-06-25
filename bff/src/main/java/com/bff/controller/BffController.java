/*
// BffController.java
package com.bff.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map; // Using Map to represent generic JSON objects for products

//@RestController
//@Slf4j
public class BffController {

    @Value("${product.service.url}")
    private String productServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate(); // RestTemplate is thread-safe, can be final
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper is thread-safe, can be final

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2AuthenticationToken authentication) {
        if (authentication == null) {
            return "Welcome! Try accessing <a href='/products'>products</a> to log in.";
        } else {
            return "Hello, " + authentication.getName() + "! " +
                    "<a href='/products'>See Products</a> | " +
                    "<a href='/logout'>Logout</a>";
        }
    }

    @GetMapping("/api/products")
    public ResponseEntity<String> getProducts(
            @RegisteredOAuth2AuthorizedClient("my-client") OAuth2AuthorizedClient authorizedClient
    ) {
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = new RestTemplate().exchange(
                productServiceUrl + "/api/products",
                HttpMethod.GET,
                request,
                String.class
        );

        return ResponseEntity.ok(response.getBody());
    }

}*/

package com.bff.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@RestController
public class MeController {

    /*@GetMapping("/me")
    public Map<String, Object> user(Principal principal) {
        return Map.of("username", principal.getName());
    }*/
    @GetMapping("/me")
    public ResponseEntity<?> getPrincipal(OAuth2AuthenticationToken auth) {
        if (auth == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        return ResponseEntity.ok(Map.of("name", auth.getPrincipal().getName()));
    }

}

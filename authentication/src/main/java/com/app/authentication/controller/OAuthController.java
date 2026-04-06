package com.app.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthController {
    @GetMapping("/login")
    public ResponseEntity<Void> customLoginPage() {
        return ResponseEntity.status(302)
                .header("Location", "/oauth2/authorization/google")
                .build();
    }
}

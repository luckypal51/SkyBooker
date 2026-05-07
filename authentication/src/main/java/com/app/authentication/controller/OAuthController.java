package com.app.authentication.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.authentication.util.ConstantValue;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class OAuthController {
	   @GetMapping("/login/google")
	    public void initiateGoogleLogin(HttpServletResponse response) throws IOException  {
	        // ✅ CORRECT — this is the URL that STARTS the OAuth2 flow
	        response.sendRedirect(ConstantValue.OAUTH2_PATH);
	    }
}

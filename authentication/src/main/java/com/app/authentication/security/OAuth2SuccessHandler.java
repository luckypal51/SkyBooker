package com.app.authentication.security;

import com.app.authentication.entity.User;
import com.app.authentication.repository.UserRepository;
import com.app.authentication.service.JwtService;
import com.app.authentication.util.ConstantValue;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    JwtService jwtSerice;

    @Autowired
    UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullName(oauthUser.getAttribute("name"));
            newUser.setRole("USER");
            newUser.setProvider("GOOGLE");
            newUser.setIsActive(true);
            newUser.setCreatedAt(LocalDate.now());
            return userRepository.save(newUser);
        });


        // Generate your JWT
        String token = jwtSerice.generateToken(email,user.getRole());


        // Redirect to your frontend or a specific landing page with the token
        String targetUrl = ConstantValue.OAUTH2_REDIRECT_URL + token;
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}

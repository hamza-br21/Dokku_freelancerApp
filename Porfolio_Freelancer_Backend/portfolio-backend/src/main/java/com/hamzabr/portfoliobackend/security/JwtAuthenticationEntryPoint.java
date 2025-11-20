package com.hamzabr.portfoliobackend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//Cette classe gère les erreurs d'authentification et renvoie une réponse 401 Unauthorized.

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        // Envoie une erreur 401 (Unauthorized) quand un utilisateur non authentifié essaie d'accéder à une ressource protégée
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
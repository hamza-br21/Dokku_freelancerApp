package com.hamzabr.portfoliobackend.config;

import com.hamzabr.portfoliobackend.security.JwtAuthenticationEntryPoint;
import com.hamzabr.portfoliobackend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Pour sécuriser des méthodes spécifiques si nécessaire
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



// Nouveaux imports pour la configuration CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays; // Pour utiliser Arrays.asList



@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // ... Injection des dépendances pour JWT Filter, UserDetailsService, etc. ...

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // --- AJOUT DE LA CONFIGURATION CORS ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Autoriser spécifiquement le port de votre frontend Vite
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); //attention tjr a verifier

        // Autoriser toutes les méthodes (POST pour login)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Autoriser les en-têtes, y compris 'Authorization' pour le token JWT
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // IMPORTANT: Permettre les identifiants (nécessaire pour les cookies ou l'authentification HTTP si utilisée)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Appliquer cette configuration à tous les chemins de l'API (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    // -------------------------------------


    // Bean pour le chiffrement des mots de passe

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Bean pour le manager d'authentification
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // Chaîne de filtres de sécurité
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())// Désactiver CSRF car on utilise JWT
                .cors(Customizer.withDefaults())// Permettre le CORS (Cross-Origin Resource Sharing)
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// Pas de sessions HTTP (JWT est sans état)
                .authorizeHttpRequests(authorize -> authorize
                        // ACCÈS PUBLIC
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/projets/**").permitAll() // Seuls les GET sont publics
                        .requestMatchers("/api/contact").permitAll()
                        // 🎯 Autoriser l'accès public pour SERVIR les fichiers
                        .requestMatchers(HttpMethod.GET, "/api/files/uploads/**").permitAll()

                        // ACCÈS ADMIN SEULEMENT (pour le POST)
                        .requestMatchers(HttpMethod.POST, "/api/files/upload").hasRole("ADMIN")
                        //.requestMatchers("/h2-console/**").permitAll() // autorise la console
                        //.anyRequest().authenticated() // le reste nécessite auth
                        // ACCÈS ADMIN SEULEMENT
                        // Sécurise les endpoints de gestion des messages et les modifications de projets
                        .requestMatchers("/api/messages/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/projets").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/projets/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/projets/**").hasRole("ADMIN")
                        // Tout le reste nécessite une authentification
                        .anyRequest().authenticated()
                );

      // Ajouter le filtre JWT pour vérifier le token avant chaque requête sécurisée
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
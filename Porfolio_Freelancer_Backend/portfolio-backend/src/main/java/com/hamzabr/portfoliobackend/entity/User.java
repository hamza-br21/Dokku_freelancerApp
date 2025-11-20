package com.hamzabr.portfoliobackend.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@Table(name = "users") // <-- AJOUTEZ OU MODIFIEZ CETTE LIGNE
public class User { // Utilisé pour l'authentification Admin
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password; // Sera stocké haché
    private String role; // Ex: "ROLE_ADMIN"
}
package com.hamzabr.portfoliobackend.dto;

import lombok.Data;

@Data // Lombok pour générer getters, setters, etc.
public class LoginRequest {
    private String username;
    private String password;
}
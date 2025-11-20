package com.hamzabr.portfoliobackend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Settings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String siteTitle;
    private String bio;
    private String logoUrl;
    private String theme;

    // Pour stocker les liens sociaux de manière structurée
    @Lob
    private String socialLinksJson;
}
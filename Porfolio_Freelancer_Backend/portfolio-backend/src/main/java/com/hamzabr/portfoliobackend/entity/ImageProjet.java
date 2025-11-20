package com.hamzabr.portfoliobackend.entity;

import com.hamzabr.portfoliobackend.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ImageProjet extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String caption; // Légende ou texte alternatif (SEO/accessibilité)

    private int orderIndex; // Permet de définir l'ordre d'affichage dans la galerie

    // Relation Many-to-One vers Projet
    @ManyToOne(fetch = FetchType.LAZY) // FetchType.LAZY pour ne pas charger le projet par défaut
    @JoinColumn(name = "projet_id") // Le nom de la colonne de clé étrangère dans la table 'image_projet'
    private Projet projet;
}
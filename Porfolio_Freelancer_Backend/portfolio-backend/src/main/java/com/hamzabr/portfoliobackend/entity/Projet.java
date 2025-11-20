package com.hamzabr.portfoliobackend.entity;

import com.hamzabr.portfoliobackend.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity // Indique que c'est une table JPA
@Data // Lombok: Getters, Setters, toString, equals/hashCode
@EqualsAndHashCode(callSuper = true) // Nécessaire car hérite de Auditable
public class Projet extends Auditable {

    @Id // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrément
    private Long id;

    private String title;

    private String shortDescription;

    // Utilisation de @Lob pour un champ de texte long dans la DB
    @Lob
    private String description;

    private String coverImageUrl; // L'image de la carte/galerie principale

    private String projectUrl; // Lien vers le projet live

    // Relation One-to-Many avec Image
    // 'mappedBy' indique le champ de l'autre côté de la relation (dans la classe ImageProjet)
    // CascadeType.ALL assure que si un Projet est supprimé, ses Images le sont aussi
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageProjet> images;
}

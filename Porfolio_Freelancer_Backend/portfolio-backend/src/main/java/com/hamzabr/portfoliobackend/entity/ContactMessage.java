package com.hamzabr.portfoliobackend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hamzabr.portfoliobackend.Auditable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
//@EntityListeners(AuditingEntityListener.class) // AJOUTER CETTE LIGNE
@EqualsAndHashCode(callSuper = true) // Important car on hérite d'Auditable
// RETIRER LES CHAMPS DE DATE et les laisser dans Auditable
public class ContactMessage extends Auditable { // N'hérite pas d'Auditable si seule la date de création est importante

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameClient;

    private String emailClient;

    private String subject;

    @Lob
    private String message;

    // Date de réception du message (ne change jamais, pas besoin d'updatedAt)
    // createdAt et updatedAt sont hérités de Auditable et remplis automatiquement.
   // @CreatedDate
    //private LocalDateTime createdAt;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonProperty("isRead") // <-- FORCE le champ JSON à être "isRead"
    private boolean isRead = false;

}

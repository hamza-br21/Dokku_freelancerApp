package com.hamzabr.portfoliobackend.service;



import com.hamzabr.portfoliobackend.entity.Projet;
import com.hamzabr.portfoliobackend.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
//Ce service gère toute la logique CRUD pour votre portfolio.
@Service // Marque la classe comme un bean de service Spring
public class ProjetService {

    private final ProjetRepository projetRepository;

    // Injection de dépendance du Repository
    @Autowired
    public ProjetService(ProjetRepository projetRepository) {
        this.projetRepository = projetRepository;
    }

    // A - MÉTHODES PUBLIQUES (GET /api/projets)
    public List<Projet> getAllProjets() {
        // Retourne la liste triée par date d'ajout ou de fin de projet
        return projetRepository.findAll();
    }

    public Optional<Projet> getProjetById(Long id) {
        return projetRepository.findById(id);
    }

    // B - MÉTHODES SÉCURISÉES (POST/PUT/DELETE /api/projets)
    // **L'ADMIN doit être authentifié pour utiliser ces méthodes**

    public Projet createProjet(Projet projet) {
        // Logique métier : validation des champs, traitement des images, etc.
        // (Ex: vérifier si le titre est unique)
        return projetRepository.save(projet);
    }

    public Projet updateProjet(Long id, Projet projetDetails) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet non trouvé pour cet id :: " + id)); // Gestion d'erreur

        // Mise à jour des champs
        projet.setTitle(projetDetails.getTitle());
        projet.setShortDescription(projetDetails.getShortDescription());
        projet.setDescription(projetDetails.getDescription());
        projet.setProjectUrl(projetDetails.getProjectUrl());
        projet.setCoverImageUrl(projetDetails.getCoverImageUrl());
        // ... Mettre à jour les autres champs...

        return projetRepository.save(projet);
    }

    public void deleteProjet(Long id) {
        // Vérifier si le projet existe avant de le supprimer pour une bonne gestion d'erreur
        if (!projetRepository.existsById(id)) {
            throw new RuntimeException("Projet non trouvé pour suppression :: " + id);
        }
        projetRepository.deleteById(id);
    }
}


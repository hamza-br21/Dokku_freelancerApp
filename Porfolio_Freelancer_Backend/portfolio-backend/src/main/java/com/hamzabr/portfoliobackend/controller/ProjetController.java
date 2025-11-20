package com.hamzabr.portfoliobackend.controller;

import com.hamzabr.portfoliobackend.entity.Projet;
import com.hamzabr.portfoliobackend.service.ProjetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projets") // Base URL pour toutes les méthodes
@CrossOrigin(origins = "http://localhost:3000") // Permet les requêtes depuis React
public class ProjetController {

    private final ProjetService projetService;

    @Autowired
    public ProjetController(ProjetService projetService) {
        this.projetService = projetService;
    }

    // ===========================================
    // 1. ENDPOINTS PUBLICS (Lecture)
    // ===========================================

    /** GET /api/projets : Récupère tous les projets pour l'affichage public */
    @GetMapping
    public List<Projet> getAllProjets() {
        return projetService.getAllProjets();
    }

    /** GET /api/projets/{id} : Récupère un projet par son ID (pour page de détail) */
    @GetMapping("/{id}")
    public ResponseEntity<Projet> getProjetById(@PathVariable Long id) {
        return projetService.getProjetById(id)
                .map(projet -> ResponseEntity.ok().body(projet)) // 200 OK
                .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }

    // ===========================================
    // 2. ENDPOINTS SÉCURISÉS (Admin - Création, Modification, Suppression)
    // ===========================================

    /** * POST /api/projets : Crée un nouveau projet.
     * **NÉCESSITE AUTHENTIFICATION ADMIN** */
    @PostMapping
    public ResponseEntity<Projet> createProjet(@RequestBody Projet projet) {
        // En cas de succès, renvoie 201 Created
        Projet createdProjet = projetService.createProjet(projet);
        return new ResponseEntity<>(createdProjet, HttpStatus.CREATED);
    }

    /** * PUT /api/projets/{id} : Met à jour un projet existant.
     * **NÉCESSITE AUTHENTIFICATION ADMIN** */
    @PutMapping("/{id}")
    public ResponseEntity<Projet> updateProjet(@PathVariable Long id, @RequestBody Projet projetDetails) {
        try {
            Projet updatedProjet = projetService.updateProjet(id, projetDetails);
            return ResponseEntity.ok(updatedProjet);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    /** * DELETE /api/projets/{id} : Supprime un projet.
     * **NÉCESSITE AUTHENTIFICATION ADMIN** */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProjet(@PathVariable Long id) {
        try {
            projetService.deleteProjet(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}

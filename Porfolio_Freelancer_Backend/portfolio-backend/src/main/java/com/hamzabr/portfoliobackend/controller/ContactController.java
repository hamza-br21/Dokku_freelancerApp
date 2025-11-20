package com.hamzabr.portfoliobackend.controller;

import com.hamzabr.portfoliobackend.entity.ContactMessage;
import com.hamzabr.portfoliobackend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api") // Utilisation d'un préfixe /api plus général
@CrossOrigin(origins = "http://localhost:3000")


public class ContactController {

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // ===========================================
    // 1. ENDPOINT PUBLIC (Soumission de message)
    // ===========================================

    /** * POST /api/contact : Reçoit un nouveau message du formulaire.
     * **ACCESSIBLE AU PUBLIC** */
    @PostMapping("/contact")
    public ResponseEntity<ContactMessage> submitContactForm(@RequestBody ContactMessage message) {
        ContactMessage savedMessage = contactService.saveMessage(message);
        // Renvoie 201 Created et le message sauvegardé (pour confirmation)
        return new ResponseEntity<>(savedMessage, HttpStatus.CREATED);
    }

    // ===========================================
    // 2. ENDPOINTS SÉCURISÉS (Consultation par l'Admin)
    // ===========================================

    /** * GET /api/messages : Récupère tous les messages reçus.
     * **NÉCESSITE AUTHENTIFICATION ADMIN** */
    @GetMapping("/messages")
    public List<ContactMessage> getAllMessages() {
        return contactService.getAllMessages();
    }

    // Classe DTO simple pour recevoir le statut de lecture
// Vous pouvez créer un fichier séparé si vous le souhaitez
    static class ReadStatusUpdate {
        public boolean isRead;
    }
    /** * PUT /api/messages/{id}/read : Marque un message comme lu.
     * **NÉCESSITE AUTHENTIFICATION ADMIN** */
    @PutMapping("/messages/{id}/read")
    public ResponseEntity<ContactMessage> markMessageAsRead(@PathVariable Long id,@RequestBody ReadStatusUpdate update) {
        try {
           // ContactMessage updatedMessage = contactService.markAsRead(id);
            ContactMessage updatedMessage = contactService.updateReadStatus(id, update.isRead);
            return ResponseEntity.ok(updatedMessage);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }

    // Optionnel : Ajouter un endpoint pour supprimer un message
    @DeleteMapping("/messages/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        contactService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
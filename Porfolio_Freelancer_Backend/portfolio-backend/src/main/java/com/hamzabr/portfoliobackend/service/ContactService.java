package com.hamzabr.portfoliobackend.service;

import com.hamzabr.portfoliobackend.entity.ContactMessage;
import com.hamzabr.portfoliobackend.repository.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ContactService {

    private final ContactMessageRepository messageRepository;

    @Autowired
    public ContactService(ContactMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // A - MÉTHODE PUBLIQUE (POST /api/contact)
    public ContactMessage saveMessage(ContactMessage message) {
        // Logique métier : validation de l'email, nettoyage du message...

        // Optionnel : Déclencher un service d'envoi d'e-mail ici
        // emailService.sendNotificationToAdmin(message);

        return messageRepository.save(message);
    }

    // B - MÉTHODE SÉCURISÉE (GET /api/messages)
    public List<ContactMessage> getAllMessages() {
        // Cela garantit que la lecture est effectuée dans un contexte propre.
        return messageRepository.findAll();
    }

   /*
    // Optionnel : Marquer un message comme lu (Admin)
    public ContactMessage markAsRead(Long id) {
        ContactMessage message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message non trouvé"));
        message.setRead(true);
        return messageRepository.save(message);
    }
    */
    public ContactMessage updateReadStatus(Long id, boolean isRead) {
        return messageRepository.findById(id)
                .map(message -> {
                    message.setRead(isRead); // <-- Appliquer le statut reçu

                    // AJOUTER UNE LIGNE POUR FORCER LE FLUSH
                    //ContactMessage updatedMessage = messageRepository.save(message);
                   // messageRepository.flush(); // FORCE l'écriture immédiate

                    //return updatedMessage;
                    return messageRepository.save(message); // <-- Enregistrer le changement

                })
                .orElseThrow(() -> new RuntimeException("Message not found with id: " + id));
    }

    public void deleteMessage(Long id) {
        // Vérifier si le message existe avant de le supprimer pour une bonne gestion d'erreur
        if (!messageRepository.existsById(id)) {
            throw new RuntimeException("Message non trouvé pour suppression :: " + id);
        }
        messageRepository.deleteById(id);
    }
}
package com.hamzabr.portfoliobackend.repository;

import com.hamzabr.portfoliobackend.entity.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// L'interface étend JpaRepository pour les opérations CRUD
@Repository
public interface ProjetRepository extends JpaRepository<Projet, Long> {
    // Méthodes de base (save, findById, findAll, deleteById) sont héritées

    // Exemple de méthode personnalisée (optionnel)
     List<Projet> findByTitleContainingIgnoreCase(String keyword);
}
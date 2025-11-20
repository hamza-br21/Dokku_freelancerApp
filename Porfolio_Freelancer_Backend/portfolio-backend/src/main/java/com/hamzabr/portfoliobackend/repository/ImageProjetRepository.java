package com.hamzabr.portfoliobackend.repository;

import com.hamzabr.portfoliobackend.entity.ImageProjet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// L'interface étend JpaRepository pour les opérations CRUD
@Repository
public interface ImageProjetRepository extends JpaRepository<ImageProjet, Long> {
    // Méthodes de base (save, findById, findAll, deleteById) sont héritées
    List<ImageProjet> findByProjetIdOrderByOrderIndexAsc(Long projectId);
}

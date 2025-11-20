package com.hamzabr.portfoliobackend.repository;



import com.hamzabr.portfoliobackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// L'interface étend JpaRepository pour les opérations CRUD
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Méthodes de base (save, findById, findAll, deleteById) sont héritées
    Optional<User> findByUsername(String username); // Très important pour l'authentification
}

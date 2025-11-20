package com.hamzabr.portfoliobackend.config;

import com.hamzabr.portfoliobackend.entity.User;
import com.hamzabr.portfoliobackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Dans un nouveau package comme 'config' ou 'initialization'
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            // **ATTENTION : Le mot de passe doit être haché !**
            admin.setPassword(passwordEncoder.encode("pass2002"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);
            System.out.println("Compte ADMIN initialisé avec succès.");
        }
    }
}
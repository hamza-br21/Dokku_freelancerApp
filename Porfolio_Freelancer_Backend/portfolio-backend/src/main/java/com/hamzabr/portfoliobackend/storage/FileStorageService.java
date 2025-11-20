package com.hamzabr.portfoliobackend.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    // Définir le répertoire de stockage dans application.properties
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        // 1. Nettoyer le nom du fichier et générer un nom unique
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID().toString() + extension;

        // 2. Créer le chemin de destination
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

        // Créer le répertoire s'il n'existe pas
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 3. Copier le fichier dans le répertoire de destination
        Path targetLocation = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // 4. Renvoyer le nom unique du fichier (pour l'URL)
        return fileName;
    }

    // NOUVELLE MÉTHODE : Permet de charger la ressource de manière cohérente
    public Path getFilePath(String fileName) {
        // Utilisez la même base de chemin configurée pour le stockage
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        return uploadPath.resolve(fileName).normalize();
    }
}
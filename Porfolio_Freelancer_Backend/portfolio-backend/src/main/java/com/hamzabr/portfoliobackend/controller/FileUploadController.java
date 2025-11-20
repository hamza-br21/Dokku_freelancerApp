package com.hamzabr.portfoliobackend.controller;

import com.hamzabr.portfoliobackend.storage.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000") // Assurez-vous d'autoriser le port React
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @Autowired
    public FileUploadController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    /**
     * POST /api/files/upload : Télécharge une image et renvoie son URL.
     * NÉCESSITE AUTHENTIFICATION ADMIN
     */
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Sauvegarde du fichier et récupération du nom unique
            String fileName = fileStorageService.storeFile(file);

            // Construire l'URL complète pour le frontend
            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/uploads/") // Endpoint de lecture public
                    .path(fileName)
                    .toUriString();
            if (fileUrl.startsWith("/")) {
                fileUrl = "http://localhost:8080" + fileUrl;
            }

            // Renvoyer l'URL comme réponse
            return ResponseEntity.ok(fileUrl);
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Échec du téléchargement du fichier : " + ex.getMessage());
        }
    }

    /**
     * GET /api/files/uploads/{fileName} : Endpoint PUBLIC pour servir les images
     */
    @GetMapping("/uploads/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
       // Path filePath = Paths.get("./uploads").toAbsolutePath().normalize().resolve(fileName).normalize();

        //  Utiliser le service pour obtenir le chemin (garantit la cohérence)
        Path filePath = fileStorageService.getFilePath(fileName);
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
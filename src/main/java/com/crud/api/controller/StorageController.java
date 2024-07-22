package com.crud.api.controller;

import com.crud.api.controller.swagger.StorageControllerSwagger;
import com.crud.api.entity.Image;
import com.crud.api.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class StorageController implements StorageControllerSwagger {

    private final StorageService storageService;

    @Override
    @PostMapping("/{userId}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Long> uploadImage(@RequestParam("image") MultipartFile file, @PathVariable Long userId) throws IOException {
        Long uploadImage = storageService.uploadImage(file, userId);
        return ResponseEntity.status(HttpStatus.OK).body(uploadImage);
    }

    @Override
    @GetMapping("/{userId}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Image> downloadImage(@PathVariable Long userId) {
        Image image = storageService.downloadImage(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(image);
    }

    @Override
    @PutMapping("/{userId}")
    @PreAuthorize(value = "hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Long> updateImage(@RequestParam("image") MultipartFile file, @PathVariable Long userId) throws IOException {
        Long updatedImage = storageService.updateImage(file, userId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedImage);
    }
}

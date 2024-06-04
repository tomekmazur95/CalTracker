package com.crud.api.service;

import com.crud.api.entity.Image;
import com.crud.api.entity.User;
import com.crud.api.error.FileNotFoundException;
import com.crud.api.error.UserNotFoundException;
import com.crud.api.repository.StorageRepository;
import com.crud.api.repository.UserRepository;
import com.crud.api.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StorageService {

    private static final String USER_NOT_FOUND = "User with id: %s not found";
    private static final String FILE_NOT_FOUND = "File for user id: %s not found";

    private final StorageRepository storageRepository;
    private final UserRepository userRepository;

    public Long uploadImage(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));
        Image image = storageRepository.save(Image.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .user(user)
                .build()
        );
        return image.getId();
    }

    public Image downloadImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format(USER_NOT_FOUND, userId)));
        Image image = storageRepository.findByUserId(userId)
                .orElseThrow(() -> new FileNotFoundException(String.format(FILE_NOT_FOUND, userId)));
        byte[] bytes = ImageUtils.decompressImage(image.getImageData());
        return new Image(image.getId(), image.getName(), image.getType(), bytes, user);
    }

    @Transactional
    public Long updateImage(MultipartFile file, Long userId) throws IOException {
        Image image = storageRepository.findByUserId(userId)
                .orElseThrow(() -> new FileNotFoundException(String.format(FILE_NOT_FOUND, userId)));

        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setImageData(ImageUtils.compressImage(file.getBytes()));

        storageRepository.save(image);
        return image.getId();
    }
}

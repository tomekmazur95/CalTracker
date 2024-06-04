package com.crud.api.repository;

import com.crud.api.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUserId(Long userId);
}

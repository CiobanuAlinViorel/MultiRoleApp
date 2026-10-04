package com.example.secu.repositories;

import com.example.secu.entities.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, UUID> {
    Optional<ResourceType> findByCode(String code);
}


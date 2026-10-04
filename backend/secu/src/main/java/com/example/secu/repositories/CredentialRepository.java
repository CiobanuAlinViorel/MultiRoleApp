package com.example.secu.repositories;

import com.example.secu.entities.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {
    List<Credential> findByUserId(UUID userId);   // List, because the mapping allows several
}

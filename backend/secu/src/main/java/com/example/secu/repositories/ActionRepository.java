package com.example.secu.repositories;

import com.example.secu.entities.Action;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ActionRepository extends JpaRepository<Action, UUID> {
    Optional<Action> findByCode(String code);
}
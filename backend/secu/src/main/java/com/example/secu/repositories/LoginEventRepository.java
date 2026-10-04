package com.example.secu.repositories;

import com.example.secu.entities.LoginEvent;
import com.example.secu.entities.LoginEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.UUID;
public interface LoginEventRepository extends JpaRepository<LoginEvent, UUID> {

    long countByAttemptedEmailAndTypeAndOccurredAtAfter(
            String email, LoginEventType type, Instant since);

    long countByIpAddressAndTypeAndOccurredAtAfter(
            String ip, LoginEventType type, Instant since);

    // retention (GDPR): delete events older than, say, 90 days
    @Modifying
    @Query("delete from LoginEvent e where e.occurredAt < :cutoff")
    int deleteOlderThan(@Param("cutoff") Instant cutoff);
}
package com.example.secu.entities;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "sessions", indexes = {
        @Index(name = "idx_sessions_user_id", columnList = "user_id"),
        @Index(name = "idx_sessions_expires_at", columnList = "expires_at")
})
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID  id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash",  nullable = false,  unique = true)
    private String tokenHash;

    private String device;

    @Column(name = "expires_at",  nullable = false)
    private Instant expiresAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    @PrePersist
    public void onCreate(){
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = Instant.now();
    }

    public Session(User user, String tokenHash, String device, Instant  expiresAt) {
        this.user = user;
        this.tokenHash = tokenHash;
        this.device = device;
        this.expiresAt = expiresAt;
    }


    public boolean isActive(Instant now) {
        return revokedAt == null && now.isBefore(expiresAt);
    }

    public void revoke(Instant now) {
        if (revokedAt == null) {
            this.revokedAt = now;
        }
    }

}

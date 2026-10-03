package com.example.secu.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "credentials")
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "password_hash",  nullable = false)
    private String passwordHash;

    @Column(name="created_at")
    private Instant createdAt;

    @Column(name="updatedAt")
    private Instant updatedAt;

    public Credential(User user, String password) {
        this.passwordHash = password;
    }

    @PrePersist
    public void onCreate(){
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = Instant.now();
    }


}

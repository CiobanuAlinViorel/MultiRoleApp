package com.example.secu.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,  nullable = false)
    private String email;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;


    @Column(name="updatedAt", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    private List<Credential> credentials = new ArrayList<>();

    @PrePersist
    public void onCreate(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void onUpdate(){
        this.updatedAt = Instant.now();
    }

    public void addCredentials(Credential credential) {
        credential.setUser(this);
        this.credentials.add(credential);
    }

    public void removeCredentials(Credential credential) {
        this.credentials.remove(credential);
        credential.setUser(null);
    }

    public List<Credential> getCredentials() {
        return Collections.unmodifiableList(credentials);
    }

    public User(String email, UserStatus userStatus) {
        this.email = email;
        this.userStatus = userStatus;
    }

}

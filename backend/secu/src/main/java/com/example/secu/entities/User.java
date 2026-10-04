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

    @Column(name="status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;


    @Column(name="updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<Credential> credentials = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<UserRole> roleAssignments = new ArrayList<>();

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

    public void addCredential(Credential credential) {
        credential.setUser(this);
        this.credentials.add(credential);
    }

    public void removeCredential(Credential credential) {
        this.credentials.remove(credential);
        credential.setUser(null);
    }


    public List<Credential> getCredentials() {
        return Collections.unmodifiableList(credentials);
    }

    public UserRole assignRole(Role role, String scopeId, Instant expiresAt) {
        UserRole ur = new UserRole(role, scopeId, expiresAt);
        ur.setUser(this);
        roleAssignments.add(ur);
        return ur;
    }

    public void revokeRole(UserRole ur) {
        roleAssignments.remove(ur);
        ur.setUser(null);
    }

    public List<UserRole> getRoleAssignments() {
        return Collections.unmodifiableList(roleAssignments);
    }

    public User(String email, UserStatus userStatus) {
        this.email = email;
        this.userStatus = userStatus;
    }

}

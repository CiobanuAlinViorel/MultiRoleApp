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
@Table(name = "user_roles",
   uniqueConstraints = @UniqueConstraint(
           name="uk_user_role_scope",
           columnNames = {"user_id", "role_id", "scope_id"}
   ),
        indexes = {
          @Index(name = "idx_user_roles_role_id", columnList = "role_id")
        }
)
public class UserRole {
    public static final String GLOBAL_SCOPE = "GLOBAL";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Setter(AccessLevel.PACKAGE)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="role_id",  nullable = false)
    private Role role;

    @Column(name="scope_id", nullable = false, length = 100)
    private String scopeId = GLOBAL_SCOPE;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name="created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name="updated_at")
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

    public UserRole(Role role, String scopeId, Instant expiresAt) {
        this.role = role;
        this.scopeId = (scopeId == null || scopeId.isBlank()) ? GLOBAL_SCOPE : scopeId;
        this.expiresAt = expiresAt;
    }

    public boolean isActive(Instant now) {
        return expiresAt == null || now.isBefore(expiresAt);
    }
}

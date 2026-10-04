package com.example.secu.entities;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "role_permissions",
    indexes = @Index(name = "idx_role_permissions_permission_id",
    columnList = "permission_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RolePermission {
   @EmbeddedId
    private RolePermissionId id = new  RolePermissionId();

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id")
    private Role role;

    @MapsId("permissionId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public RolePermission(Role role, Permission permission, Instant expiresAt) {
        this.role = role;
        this.permission = permission;
        this.expiresAt = expiresAt;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    public boolean isActive(Instant now) {
        return expiresAt == null || now.isBefore(expiresAt);
    }

    public void expireAt(Instant newExpiry) {
        this.expiresAt = newExpiry;
    }
}

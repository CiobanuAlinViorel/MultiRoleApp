package com.example.secu.repositories;
import com.example.secu.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    @Query("select ur from UserRole ur join fetch ur.role where ur.user.id = :userId")
    List<UserRole> findAllByUserIdWithRole(@Param("userId") UUID userId);

    boolean existsByUserIdAndRoleIdAndScopeId(UUID userId, UUID roleId, String scopeId);

    long countByRoleId(UUID roleId);     // block deleting a role that is still worn

    @Modifying
    @Query("delete from UserRole ur where ur.expiresAt < :cutoff")
    int deleteExpiredBefore(@Param("cutoff") Instant cutoff);

    // the heart of can(...): all active permission codes for a user at a scope
    @Query("""
        select distinct p.code
        from UserRole ur
        join RolePermission rp on rp.role = ur.role
        join rp.permission p
        where ur.user.id = :userId
          and (ur.scopeId = :scopeId or ur.scopeId = :globalScope)
          and (ur.expiresAt is null or ur.expiresAt > :now)
          and (rp.expiresAt is null or rp.expiresAt > :now)
        """)
    Set<String> findPermissionCodes(@Param("userId") UUID userId,
                                    @Param("scopeId") String scopeId,
                                    @Param("globalScope") String globalScope,
                                    @Param("now") Instant now);

    default Set<String> findActivePermissionCodes(UUID userId, String scopeId, Instant now) {
        return findPermissionCodes(userId, scopeId, UserRole.GLOBAL_SCOPE, now);
    }
}

package com.example.secu.repositories;
import com.example.secu.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
public interface PermissionRepository extends JpaRepository<Permission, UUID> {

    Optional<Permission> findByCode(String code);

    List<Permission> findByResourceTypeId(UUID resourceTypeId);

    // the grid for the admin screen, in one query
    @Query("""
        select p from Permission p
        join fetch p.resourceType rt
        join fetch p.action a
        order by rt.code, a.code
        """)
    List<Permission> findAllForGrid();

    // keys no longer found in the code at startup
    List<Permission> findByLastSeenAtBefore(Instant cutoff);
}
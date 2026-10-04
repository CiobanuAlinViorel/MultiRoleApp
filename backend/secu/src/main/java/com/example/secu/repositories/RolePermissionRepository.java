package com.example.secu.repositories;

import com.example.secu.entities.RolePermission;
import com.example.secu.entities.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RolePermissionRepository
        extends JpaRepository<RolePermission, RolePermissionId> {

    @Query("select rp from RolePermission rp join fetch rp.permission where rp.role.id = :roleId")
    List<RolePermission> findAllByRoleIdWithPermission(@Param("roleId") UUID roleId);

    long countByPermissionId(UUID permissionId);    // how many hats hold this key

    @Modifying
    @Query("delete from RolePermission rp where rp.role.id = :roleId")
    int deleteAllByRoleId(@Param("roleId") UUID roleId);
}
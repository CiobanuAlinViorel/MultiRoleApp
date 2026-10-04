package com.example.secu.repositories;

import com.example.secu.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    // join fetch: you need the user right away, so avoid a second query
    @Query("select s from Session s join fetch s.user where s.tokenHash = :hash")
    Optional<Session> findWithUserByTokenHash(@Param("hash") String hash);

    @Query("""
        select s from Session s
        where s.user.id = :userId and s.revokedAt is null and s.expiresAt > :now
        order by s.createdAt desc
        """)
    List<Session> findActiveByUserId(@Param("userId") UUID userId, @Param("now") Instant now);

    // "log out everywhere"
    @Modifying(clearAutomatically = true)
    @Query("""
        update Session s set s.revokedAt = :now, s.updatedAt = :now
        where s.user.id = :userId and s.revokedAt is null
        """)
    int revokeAllByUserId(@Param("userId") UUID userId, @Param("now") Instant now);

    // nightly cleanup job
    @Modifying
    @Query("""
        delete from Session s
        where s.expiresAt < :cutoff
           or (s.revokedAt is not null and s.revokedAt < :cutoff)
        """)
    int deleteEndedBefore(@Param("cutoff") Instant cutoff);
}

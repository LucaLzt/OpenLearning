package com.projects.openlearning.identity.internal.repository;

import com.projects.openlearning.identity.internal.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {

    Optional<Session> findByToken(String token);

    @Modifying
    @Query("UPDATE Session s SET s.revoked = true WHERE s.userId = :userId")
    void revokeAllSessions(UUID userId);

}

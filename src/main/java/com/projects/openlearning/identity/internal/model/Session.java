package com.projects.openlearning.identity.internal.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sessions")
@EntityListeners(AuditingEntityListener.class)
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, updatable = false)
    private UUID userId;

    @Column(nullable = false, unique = true, length = 512)
    private String token;

    @Column(name = "device_info", nullable = false, updatable = false)
    private String deviceInfo;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    @Builder.Default
    private Boolean revoked = false;

    @Column(name = "parent_session_id")
    private UUID parentSessionId;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return id != null && id.equals(session.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Factory Methods to create sessions with different scenarios
     */
    public static Session createFirstSession(UUID userId, String token, String deviceInfo, Instant expiresAt) {
        return Session.builder()
                .userId(userId)
                .token(token)
                .deviceInfo(deviceInfo)
                .expiresAt(expiresAt)
                .revoked(false)
                .parentSessionId(null)
                .build();
    }

    public static Session rotate(Session parent, String newToken, Instant newExpiration) {
        parent.setRevoked(true);    // Revoke the parent session
        return Session.builder()
                .userId(parent.getUserId())
                .deviceInfo(parent.getDeviceInfo())
                .parentSessionId(parent.getId())
                .token(newToken)
                .expiresAt(newExpiration)
                .revoked(false)
                .build();
    }

    public boolean isRevoked() {
        return Boolean.TRUE.equals(this.revoked);
    }

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    public void revoke() {
        this.revoked = true;
    }
}

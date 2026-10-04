package com.example.secu.entities;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "login_events", indexes = {
        @Index(name = "idx_login_events_user_id_time", columnList = "user_id, occurred_at"),
        @Index(name = "idx_login_events_ip_address_time", columnList = "ip_address, occurred_at"),
        @Index(name = "idx_login_events_email_time", columnList = "attempted_email, occurred_at")
})
public class LoginEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID  id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", updatable = false)
    private User user;

    @Column(name="attempted_email", nullable=false, updatable=false, length=255)
    private String attemptedEmail;

    @Column(name = "ip_address", length = 45, updatable=false)
    private String ipAddress;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false, length = 30)
    private LoginEventType type;


    @Column(name="occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;

    public boolean isSuccess(){
        return this.type ==  LoginEventType.SUCCESS;
    }

    private static String normalize(String email) {
        String e = email == null ? "" : email.trim().toLowerCase();
        return e.length() > 254 ? e.substring(0, 254) : e;
    }

    public LoginEvent(User user, String ipAddress,  LoginEventType type, Instant occurredAt, String attemptedEmail ) {
        this.user = user;
        this.ipAddress = ipAddress;
        this.type = type;
        this.occurredAt = occurredAt;
        this.attemptedEmail = normalize(attemptedEmail);
    }
}

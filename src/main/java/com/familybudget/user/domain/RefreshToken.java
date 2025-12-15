package com.familybudget.user.domain;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.Instant; import java.util.UUID;

@Entity @Table(name="refresh_tokens")
@Getter @Setter
public class RefreshToken {
    @Id @Column(columnDefinition="uuid") private UUID id;
    @PrePersist public void pre(){ if(id==null) id = UUID.randomUUID(); }

    @ManyToOne(optional=false) @JoinColumn(name="user_id")
    private User user;

    @Column(nullable=false, unique=true, length=512)
    private String token;

    @Column(nullable=false) private Instant expiresAt;
    @Column(nullable=false) private boolean revoked = false;
    @Column(nullable=false) private Instant createdAt = Instant.now();
}

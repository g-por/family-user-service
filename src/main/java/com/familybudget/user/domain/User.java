package com.familybudget.user.domain;

import jakarta.persistence.*;
import lombok.Getter; import lombok.Setter;
import java.time.Instant; import java.util.*;

@Entity @Table(name="users")
@Getter @Setter
public class User {
    @Id @Column(columnDefinition="uuid") private UUID id;
    @PrePersist public void pre(){ if(id==null) id = UUID.randomUUID(); }

    @Column(nullable=false, unique=true) private String email;
    @Column(name="password_hash", nullable=false) private String passwordHash;

    @Column(name="nickname", nullable = false)
    private String nickname;

    @Column(name="avatar_url")
    private String avatarUrl;

    private String city;

    @Column(name = "phone", nullable = false)
    private String phone;

    private boolean enabled = true;
    @Column(nullable=false) private Instant createdAt = Instant.now();
    @Column(nullable=false) private Instant updatedAt = Instant.now();
    @PreUpdate public void touch(){ updatedAt = Instant.now(); }
}

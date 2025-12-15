package com.familybudget.user.repo;
import com.familybudget.user.domain.RefreshToken; import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; import java.util.UUID;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
}

package com.familybudget.user.repo;
import com.familybudget.user.domain.*; import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
}

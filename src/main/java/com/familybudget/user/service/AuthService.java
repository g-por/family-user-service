package com.familybudget.user.service;
import java.time.temporal.ChronoUnit;

import com.familybudget.user.domain.RefreshToken;
import com.familybudget.user.domain.User;
import com.familybudget.user.dto.*;
import com.familybudget.user.repo.RefreshTokenRepo;
import com.familybudget.user.repo.RoleRepo;
import com.familybudget.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*; import java.util.UUID;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepo users; private final RoleRepo roles;
    private final RefreshTokenRepo rts; private final PasswordEncoder pe; private final JwtService jwt;

    @Value("${jwt.refresh-token-ttl-days}") private long refreshTtlDays;

    @Transactional
    public void register(RegisterRequest r){
        if (users.existsByEmailIgnoreCase(r.email()))
            throw new IllegalArgumentException("Email already used");
        var u = new User();
        u.setEmail(r.email().trim().toLowerCase());
        u.setPasswordHash(pe.encode(r.password()));
        u.setFullName(r.fullName());
        u.getRoles().add(roles.findByName("ROLE_USER").orElseThrow());
        users.save(u);
    }

    @Transactional
    public TokenPair login(LoginRequest r){
        var u = users.findByEmailIgnoreCase(r.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!pe.matches(r.password(), u.getPasswordHash()))
            throw new BadCredentialsException("Bad credentials");

        var access = jwt.generateAccess(u);
        var refresh = UUID.randomUUID()+"."+UUID.randomUUID();

        var rt = new RefreshToken();
        rt.setUser(u); rt.setToken(refresh);
        rt.setExpiresAt(Instant.now().plus(refreshTtlDays, ChronoUnit.DAYS));
        rts.save(rt);

        return new TokenPair(access, refresh);
    }

    @Transactional
    public TokenPair refresh(String refreshToken){
        var rt = rts.findByToken(refreshToken).orElseThrow(() -> new BadCredentialsException("Bad refresh"));
        if (rt.isRevoked() || rt.getExpiresAt().isBefore(Instant.now()))
            throw new BadCredentialsException("Refresh expired");
        var access = jwt.generateAccess(rt.getUser());
        return new TokenPair(access, refreshToken);
    }

    @Transactional
    public void logout(String refreshToken){
        rts.findByToken(refreshToken).ifPresent(x->{ x.setRevoked(true); rts.save(x); });
    }
}

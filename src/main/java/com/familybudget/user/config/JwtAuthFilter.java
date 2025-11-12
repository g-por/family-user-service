package com.familybudget.user.config;

import com.familybudget.user.domain.User;
import com.familybudget.user.repo.UserRepo;
import com.familybudget.user.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwt;
    private final UserRepo users;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            String token = h.substring(7);
            try {
                // Do not overwrite existing authentication
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UUID uid = UUID.fromString(jwt.getSubject(token));
                    List<String> roles = jwt.getRoles(token);
                    if (!roles.isEmpty()) {
                        var auths = roles.stream().map(SimpleGrantedAuthority::new).toList();
                        var principal = users.findById(uid).orElse(null); // optional for user details; null principal also works
                        var auth = new UsernamePasswordAuthenticationToken(principal, null, auths);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        users.findById(uid).ifPresent(u -> {
                            var auths = u.getRoles().stream().map(r -> new SimpleGrantedAuthority(r.getName())).toList();
                            var auth = new UsernamePasswordAuthenticationToken(u, null, auths);
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        });
                    }
                }
            } catch (Exception e) {
                // Log the error properly or handle it
                logger.debug("JWT validation error: {}", e.getMessage());
            }
        }
        chain.doFilter(req, res);
    }
}
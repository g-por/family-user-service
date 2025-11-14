package com.familybudget.user.service;
import java.time.temporal.ChronoUnit;

import com.familybudget.user.domain.Role;
import com.familybudget.user.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.*;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {
    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.issuer}") private String issuer;
    @Value("${jwt.access-token-ttl-min}") private long accessTtlMin;

    private Key key(){ return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }

    public String generateAccess(User u){
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(u.getId().toString())
                .setIssuer(issuer)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(accessTtlMin, ChronoUnit.MINUTES)))
                .claim("email", u.getEmail())
                .claim("roles", u.getRoles().stream().map(Role::getName).toList())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseAndValidate(String token){
        JwtParser parser = Jwts.parserBuilder()
                .requireIssuer(issuer)
                .setSigningKey(key())
                .build();
        return parser.parseClaimsJws(token).getBody();
    }

    public String getSubject(String token){
        return parseAndValidate(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token){
        Object claim = parseAndValidate(token).get("roles");
        if (claim instanceof List<?> list) {
            return (List<String>) list;
        }
        return List.of();
    }
}

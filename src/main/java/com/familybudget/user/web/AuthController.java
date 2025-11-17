package com.familybudget.user.web;

import com.familybudget.user.dto.*;
import com.familybudget.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController @RequestMapping("/api/auth") @RequiredArgsConstructor
public class AuthController {
    private final AuthService auth;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest req){
        auth.register(req); return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req){
        return AuthResponse.of(auth.login(req));
    }

    @PostMapping("/refresh")
    public AuthResponse refresh(@RequestHeader("X-Refresh-Token") String rt){
        return AuthResponse.of(auth.refresh(rt));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("X-Refresh-Token") String rt){
        auth.logout(rt); return ResponseEntity.noContent().build();
    }
}

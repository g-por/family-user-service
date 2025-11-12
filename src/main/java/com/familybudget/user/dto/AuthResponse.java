package com.familybudget.user.dto;
public record AuthResponse(String tokenType, TokenPair tokens) {
    public static AuthResponse of(TokenPair p){ return new AuthResponse("Bearer", p); }
}

package com.familybudget.user.dto;
import java.util.UUID;

public record MeResponse(UUID id, String email, String nickname, String avatarUrl, String city) {}

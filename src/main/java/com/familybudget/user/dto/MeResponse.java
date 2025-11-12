package com.familybudget.user.dto;
import java.util.UUID;
public record MeResponse(UUID id, String email, String fullName, String avatarUrl, String city) {}

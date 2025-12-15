package com.familybudget.user.dto;
import jakarta.validation.constraints.NotBlank;

public record UpdateMeRequest(@NotBlank String nickname, String avatarUrl, String city) {}

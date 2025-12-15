package com.familybudget.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank String password,
        @NotBlank String nickname,
        @Pattern(regexp = "^\\+380\\d{9}$", message = "Phone must be Ukrainian +380XXXXXXXXX") String phone,
        String avatarUrl,
        String city
) {}

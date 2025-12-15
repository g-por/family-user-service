package com.familybudget.user.dto.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreatedEvent{
    private static final long serialVersionUID = 1L;
    private UUID userId;
    private String email;
    private String nickname;
    private String phone;
}
package com.familybudget.user.service;

import com.familybudget.user.domain.User;
import com.familybudget.user.dto.*;
import com.familybudget.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class MeService {
    private final UserRepo users; private final PasswordEncoder pe;

    public MeResponse me(User u){
        return new MeResponse(u.getId(), u.getEmail(), u.getFullName(), u.getAvatarUrl(), u.getCity());
    }

    @Transactional
    public MeResponse update(User u, UpdateMeRequest r){
        u.setFullName(r.fullName()); u.setAvatarUrl(r.avatarUrl()); u.setCity(r.city());
        users.save(u); return me(u);
    }

    @Transactional
    public void changePassword(User u, ChangePasswordRequest r){
        if (!pe.matches(r.oldPassword(), u.getPasswordHash()))
            throw new BadCredentialsException("Old password mismatch");
        u.setPasswordHash(pe.encode(r.newPassword())); users.save(u);
    }
}

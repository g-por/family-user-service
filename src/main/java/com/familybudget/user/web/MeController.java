package com.familybudget.user.web;

import com.familybudget.user.domain.User;
import com.familybudget.user.dto.*;
import com.familybudget.user.service.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api") @RequiredArgsConstructor
public class MeController {
    private final MeService me;

    @GetMapping("/me")
    public MeResponse get(@AuthenticationPrincipal User u){ return me.me(u); }

    @PutMapping("/me")
    public MeResponse update(@AuthenticationPrincipal User u, @RequestBody UpdateMeRequest r){
        return me.update(u, r);
    }

    @PatchMapping("/me/password")
    public ResponseEntity<Void> pwd(@AuthenticationPrincipal User u, @RequestBody ChangePasswordRequest r){
        me.changePassword(u, r); return ResponseEntity.noContent().build();
    }
}

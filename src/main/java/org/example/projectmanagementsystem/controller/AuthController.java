package org.example.projectmanagementsystem.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.dto.request.LoginRequest;
import org.example.projectmanagementsystem.dto.request.RegisterRequest;
import org.example.projectmanagementsystem.dto.response.AuthResponse;
import org.example.projectmanagementsystem.enums.UserRole;
import org.example.projectmanagementsystem.service.temp.UserService;
import org.example.projectmanagementsystem.util.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@Tag(name = "Auth", description = "Autentifikatsiya va rol boshqaruvi")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Yangi foydalanuvchi ro'yxatdan o'tkazish")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @Operation(summary = "Login va JWT token olish")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @Operation(summary = "ADMIN orqali userga hohlagan roligizni bering")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/users/{id}/role")
    public ResponseEntity<String> assignRole(@PathVariable Long id, @RequestParam UserRole role){
        return ResponseEntity.ok(userService.assignRole(id,role));
    }


    @Operation(summary = "Tizimdan chiqish")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER', 'PM')")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() throws AccessDeniedException {
        String username = SecurityUtils.getCurrentUsername();
        return ResponseEntity.ok(userService.logout(username));
    }
}

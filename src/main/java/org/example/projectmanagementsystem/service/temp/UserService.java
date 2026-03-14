package org.example.projectmanagementsystem.service.temp;

import org.example.projectmanagementsystem.dto.request.LoginRequest;
import org.example.projectmanagementsystem.dto.request.RegisterRequest;
import org.example.projectmanagementsystem.dto.response.AuthResponse;
import org.example.projectmanagementsystem.enums.UserRole;

import javax.swing.*;

public interface UserService {

    String register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    String assignRole(Long userId, UserRole role);

    String logout(String username);
}

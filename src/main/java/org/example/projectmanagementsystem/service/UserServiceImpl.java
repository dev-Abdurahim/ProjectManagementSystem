package org.example.projectmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.config.UserDetailsImpl;
import org.example.projectmanagementsystem.config.jwt.JwtService;
import org.example.projectmanagementsystem.dto.request.LoginRequest;
import org.example.projectmanagementsystem.dto.request.RegisterRequest;
import org.example.projectmanagementsystem.dto.response.AuthResponse;
import org.example.projectmanagementsystem.entity.User;
import org.example.projectmanagementsystem.enums.UserRole;
import org.example.projectmanagementsystem.exception.ApiException;
import org.example.projectmanagementsystem.exception.ErrorCode;
import org.example.projectmanagementsystem.repository.UserRepository;
import org.example.projectmanagementsystem.service.temp.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @Override
    public String register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new ApiException(
                    ErrorCode.USERNAME_ALREADY_EXISTS,
                    "Username already exists",
                    HttpStatus.BAD_REQUEST

            );
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.getRoles().add(UserRole.USER);

        userRepository.save(user);

        return "User successfully registered";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            var authentication  = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            return AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .username(userDetails.getUsername())
                    .build();

        }catch (BadCredentialsException ex){
            throw new ApiException(
                    ErrorCode.INVALID_CREDENTIALS,
                    "Invalid username or password",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @Override
    public String assignRole(Long userId, UserRole role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found with id: " + userId,
                        HttpStatus.NOT_FOUND
                ));
        user.getRoles().clear();
        user.getRoles().add(role);

        userRepository.save(user);
        return "Role successfully assigned: " + role.name();
    }
}

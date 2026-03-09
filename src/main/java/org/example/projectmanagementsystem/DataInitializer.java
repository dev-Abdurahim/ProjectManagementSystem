package org.example.projectmanagementsystem;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.projectmanagementsystem.entity.User;
import org.example.projectmanagementsystem.enums.UserRole;
import org.example.projectmanagementsystem.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin(){
        if(userRepository.existsByUsername("admin")){
            return;
        }
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("Admin123"));
        admin.setFullName("System Admin");
        admin.setRoles(Set.of(UserRole.ADMIN));
        userRepository.save(admin);
        System.out.println("ADMIN USER CREATED");

    }
}

package org.example.projectmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.projectmanagementsystem.entity.base.BaseEntity;
import org.example.projectmanagementsystem.enums.UserRole;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 100)
    private String fullName;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }
}

package org.example.projectmanagementsystem.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.projectmanagementsystem.config.UserDetailsImpl;
import org.example.projectmanagementsystem.entity.User;
import org.example.projectmanagementsystem.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.nio.file.AccessDeniedException;

//SecurityContext dan current user ni olish ishlatiladi
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtils {

    /**
     * Joriy authenticated user ni qaytaradi.
     */
    public static User getCurrentUser() throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new AccessDeniedException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        if(!(principal instanceof UserDetailsImpl userDetails)){
            throw new AccessDeniedException("Invalid authentication principal");
        }
        return userDetails.getUser();
    }

    /**
     * Joriy user ning username ini qaytaradi.
     */

    public static String getCurrentUsername() throws AccessDeniedException {
        return getCurrentUser().getUsername();
    }

    /**
     * Joriy user ning ID sini qaytaradi.
     */

    public static Long getCurrentUserId() throws AccessDeniedException{
        return getCurrentUser().getId();
    }

    /**
     * Joriy user berilgan rolga ega ekanligini tekshiradi.
     */

    public static boolean hasRole(UserRole role){
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role.name()));
    }


}

package com.jellybrains.quietspace_backend_ms.authorization_service.util;

import com.jellybrains.quietspace_backend_ms.authorization_service.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class JwtUtil {

    private static final String REALM_ACCESS = "realm_access";
    private static final String ROLES = "roles";
    private static final String EMAIL = "email";

    public static User getUserFromToken(Jwt jwt) {
        return User.builder()
                .id(getSubject(jwt))
                .email(jwt.getClaim(EMAIL))
                .build();
    }

    public static String getSubject(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    public static List<SimpleGrantedAuthority> extractResourceRoles(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS);

        if (realmAccess == null) {
            return List.of();
        }

        Collection<String> resourceRoles = (Collection<String>) realmAccess.get(ROLES);

        return resourceRoles
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}


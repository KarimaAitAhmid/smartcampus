package ma.ensa.smartcampus.dto;

import ma.ensa.smartcampus.enums.Role;

public record LoginResponse(
        String token,
        String tokenType,
        long expiresIn,
        String id,
        String nom,
        String email,
        Role role
) {
}

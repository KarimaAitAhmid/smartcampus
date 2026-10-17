package ma.ensa.smartcampus.dto;

import ma.ensa.smartcampus.enums.Role;

public record UtilisateurResponse(String id, String nom, String email, Role role) {
}
package ma.ensa.smartcampus.controller;

import jakarta.validation.Valid;
import ma.ensa.smartcampus.dto.LoginRequest;
import ma.ensa.smartcampus.dto.LoginResponse;
import ma.ensa.smartcampus.dto.UtilisateurResponse;
import ma.ensa.smartcampus.entity.Utilisateur;
import ma.ensa.smartcampus.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    // JWT sans état : la déconnexion consiste à supprimer le token côté client
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public UtilisateurResponse me(Authentication authentication) {
        Utilisateur u = authService.trouverParId(authentication.getName());
        return new UtilisateurResponse(u.getId(), u.getNom(), u.getEmail(), u.getRole());
    }
}

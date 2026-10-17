package ma.ensa.smartcampus.service;

import ma.ensa.smartcampus.dto.LoginRequest;
import ma.ensa.smartcampus.dto.LoginResponse;
import ma.ensa.smartcampus.entity.Utilisateur;
import ma.ensa.smartcampus.exception.CompteBloqueException;
import ma.ensa.smartcampus.exception.IdentifiantsInvalidesException;
import ma.ensa.smartcampus.repository.UtilisateurRepository;
import ma.ensa.smartcampus.security.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Locale;

@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final String emailDomain;

    public AuthService(UtilisateurRepository utilisateurRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       @Value("${app.email-domain}") String emailDomain) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.emailDomain = emailDomain.toLowerCase(Locale.ROOT);
    }

    public LoginResponse login(LoginRequest request) {
        Utilisateur u = authentifier(request.email(), request.password());
        return new LoginResponse(
                jwtService.genererToken(u),
                "Bearer",
                jwtService.getExpirationMs() / 1000,
                u.getId(), u.getNom(), u.getEmail(), u.getRole());
    }

    public Utilisateur authentifier(String email, String password) {
        String emailNormalise = email.trim().toLowerCase(Locale.ROOT);
        Instant maintenant = Instant.now();

        if (!emailNormalise.endsWith("@" + emailDomain)) {
            throw new IdentifiantsInvalidesException();
        }

        Utilisateur utilisateur = utilisateurRepository
                .findByEmail(emailNormalise)
                .orElseThrow(IdentifiantsInvalidesException::new);

        if (utilisateur.estBloque(maintenant)) {
            throw new CompteBloqueException();
        }

        // Blocage expiré : on repart de zéro
        if (utilisateur.getBloqueJusqua() != null) {
            utilisateur.reinitialiserEchecs();
            utilisateurRepository.save(utilisateur);
        }

        if (!utilisateur.isActif()) {
            throw new IdentifiantsInvalidesException();
        }

        if (!passwordEncoder.matches(password, utilisateur.getPasswordHash())) {
            utilisateur.enregistrerEchec(maintenant);
            utilisateurRepository.save(utilisateur);

            if (utilisateur.estBloque(maintenant)) {
                throw new CompteBloqueException();
            }
            throw new IdentifiantsInvalidesException();
        }

        utilisateur.reinitialiserEchecs();
        utilisateurRepository.save(utilisateur);
        return utilisateur;
    }

    public Utilisateur trouverParId(String id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(IdentifiantsInvalidesException::new);
    }
}
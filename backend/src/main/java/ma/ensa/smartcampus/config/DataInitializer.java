package ma.ensa.smartcampus.config;

import ma.ensa.smartcampus.entity.Utilisateur;
import ma.ensa.smartcampus.enums.Role;
import ma.ensa.smartcampus.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UtilisateurRepository utilisateurRepository,
                           PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        creerSiAbsent("Administrateur", "admin@smartcampus.local", "Admin@2026", Role.ADMIN);
        creerSiAbsent("Étudiant Test", "etudiant@smartcampus.local", "Etudiant@2026", Role.ETUDIANT);
    }

    private void creerSiAbsent(String nom, String email, String motDePasse, Role role) {
        if (utilisateurRepository.existsByEmail(email)) {
            return;
        }
        Utilisateur u = new Utilisateur();
        u.setNom(nom);
        u.setEmail(email);
        u.setPasswordHash(passwordEncoder.encode(motDePasse));
        u.setRole(role);
        u.setActif(true);
        utilisateurRepository.save(u);
        System.out.println("Compte créé : " + email);
    }
}

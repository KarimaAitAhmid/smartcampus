package ma.ensa.smartcampus.service;

import ma.ensa.smartcampus.entity.Utilisateur;
import ma.ensa.smartcampus.enums.Role;
import ma.ensa.smartcampus.exception.CompteBloqueException;
import ma.ensa.smartcampus.exception.IdentifiantsInvalidesException;
import ma.ensa.smartcampus.repository.UtilisateurRepository;
import ma.ensa.smartcampus.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    private static final String EMAIL = "etudiant@smartcampus.local";
    private static final String MDP = "Etudiant@2026";

    @Mock
    private UtilisateurRepository repository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private AuthService service;
    private Utilisateur utilisateur;

    @BeforeEach
    void init() {
        JwtService jwt = new JwtService("test-secret-key-for-smartcampus-0123456789", 60_000);
        service = new AuthService(repository, encoder, jwt, "smartcampus.local");

        utilisateur = new Utilisateur();
        utilisateur.setNom("Étudiant");
        utilisateur.setEmail(EMAIL);
        utilisateur.setPasswordHash(encoder.encode(MDP));
        utilisateur.setRole(Role.ETUDIANT);
        utilisateur.setActif(true);
    }

    private void utilisateurExiste() {
        when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(utilisateur));
    }

    @Test
    void connexionReussie() {
        utilisateurExiste();
        Utilisateur u = service.authentifier(EMAIL, MDP);

        assertThat(u.getEmail()).isEqualTo(EMAIL);
        assertThat(u.getNbEchecs()).isZero();
    }

    @Test
    void emailEstNormalise() {
        utilisateurExiste();
        assertThat(service.authentifier("  ETUDIANT@SmartCampus.local ", MDP)).isNotNull();
    }

    @Test
    void emailInconnuRefuse() {
        when(repository.findByEmail("inconnu@smartcampus.local")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.authentifier("inconnu@smartcampus.local", MDP))
                .isInstanceOf(IdentifiantsInvalidesException.class);
    }

    @Test
    void domaineNonAutoriseRefuse() {
        assertThatThrownBy(() -> service.authentifier("test@gmail.com", MDP))
                .isInstanceOf(IdentifiantsInvalidesException.class);
    }

    @Test
    void mauvaisMotDePasseIncrementeLeCompteur() {
        utilisateurExiste();

        assertThatThrownBy(() -> service.authentifier(EMAIL, "faux"))
                .isInstanceOf(IdentifiantsInvalidesException.class);
        assertThat(utilisateur.getNbEchecs()).isEqualTo(1);
    }

    @Test
    void troisiemeEchecBloqueLeCompte() {
        utilisateurExiste();

        assertThatThrownBy(() -> service.authentifier(EMAIL, "faux"))
                .isInstanceOf(IdentifiantsInvalidesException.class);
        assertThatThrownBy(() -> service.authentifier(EMAIL, "faux"))
                .isInstanceOf(IdentifiantsInvalidesException.class);
        assertThatThrownBy(() -> service.authentifier(EMAIL, "faux"))
                .isInstanceOf(CompteBloqueException.class);

        assertThat(utilisateur.estBloque(Instant.now())).isTrue();
    }

    @Test
    void compteBloqueRefuseMemeAvecBonMotDePasse() {
        for (int i = 0; i < 3; i++) utilisateur.enregistrerEchec(Instant.now());
        utilisateurExiste();

        assertThatThrownBy(() -> service.authentifier(EMAIL, MDP))
                .isInstanceOf(CompteBloqueException.class);
    }

    @Test
    void connexionReussieRemetLeCompteurAZero() {
        utilisateur.enregistrerEchec(Instant.now());
        utilisateur.enregistrerEchec(Instant.now());
        utilisateurExiste();

        service.authentifier(EMAIL, MDP);

        assertThat(utilisateur.getNbEchecs()).isZero();
    }

    @Test
    void blocageExpireAutoriseLaConnexion() {
        Instant ilYaUneHeure = Instant.now().minusSeconds(3600);
        for (int i = 0; i < 3; i++) utilisateur.enregistrerEchec(ilYaUneHeure);
        utilisateurExiste();

        Utilisateur u = service.authentifier(EMAIL, MDP);

        assertThat(u.getNbEchecs()).isZero();
        assertThat(u.getBloqueJusqua()).isNull();
    }

    @Test
    void compteDesactiveRefuse() {
        utilisateur.setActif(false);
        utilisateurExiste();

        assertThatThrownBy(() -> service.authentifier(EMAIL, MDP))
                .isInstanceOf(IdentifiantsInvalidesException.class);
    }
}
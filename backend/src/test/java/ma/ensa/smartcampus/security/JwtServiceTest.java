package ma.ensa.smartcampus.security;

import io.jsonwebtoken.Claims;
import ma.ensa.smartcampus.entity.Utilisateur;
import ma.ensa.smartcampus.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET = "test-secret-key-for-smartcampus-0123456789";
    private Utilisateur utilisateur;

    @BeforeEach
    void init() {
        utilisateur = new Utilisateur();
        utilisateur.setEmail("admin@smartcampus.local");
        utilisateur.setRole(Role.ADMIN);
        ReflectionTestUtils.setField(utilisateur, "id", "abc123");
    }

    @Test
    void tokenGenereContientLesBonnesInformations() {
        JwtService jwt = new JwtService(SECRET, 60_000);
        String token = jwt.genererToken(utilisateur);

        Claims claims = jwt.lireClaims(token);
        assertThat(claims.getSubject()).isEqualTo("abc123");
        assertThat(claims.get("role", String.class)).isEqualTo("ADMIN");
        assertThat(claims.get("email", String.class)).isEqualTo("admin@smartcampus.local");
        assertThat(jwt.estValide(token)).isTrue();
    }

    @Test
    void tokenExpireEstInvalide() {
        JwtService jwt = new JwtService(SECRET, -1000);
        assertThat(jwt.estValide(jwt.genererToken(utilisateur))).isFalse();
    }

    @Test
    void tokenModifieEstInvalide() {
        JwtService jwt = new JwtService(SECRET, 60_000);
        String token = jwt.genererToken(utilisateur);
        assertThat(jwt.estValide(token + "x")).isFalse();
    }

    @Test
    void tokenSigneAvecAutreSecretEstInvalide() {
        JwtService autre = new JwtService("another-secret-key-for-smartcampus-9876543210", 60_000);
        JwtService jwt = new JwtService(SECRET, 60_000);
        assertThat(jwt.estValide(autre.genererToken(utilisateur))).isFalse();
    }
}
package ma.ensa.smartcampus.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class UtilisateurTest {

    @Test
    void deuxEchecsNeBloquentPas() {
        Utilisateur u = new Utilisateur();
        Instant now = Instant.now();
        u.enregistrerEchec(now);
        u.enregistrerEchec(now);

        assertThat(u.getNbEchecs()).isEqualTo(2);
        assertThat(u.estBloque(now)).isFalse();
    }

    @Test
    void troisiemeEchecBloquePendant15Minutes() {
        Utilisateur u = new Utilisateur();
        Instant now = Instant.now();
        for (int i = 0; i < 3; i++) u.enregistrerEchec(now);

        assertThat(u.estBloque(now)).isTrue();
        assertThat(u.estBloque(now.plusSeconds(14 * 60))).isTrue();
        assertThat(u.estBloque(now.plusSeconds(15 * 60 + 1))).isFalse();
    }

    @Test
    void reinitialiserRemetLeCompteurAZero() {
        Utilisateur u = new Utilisateur();
        Instant now = Instant.now();
        for (int i = 0; i < 3; i++) u.enregistrerEchec(now);

        u.reinitialiserEchecs();

        assertThat(u.getNbEchecs()).isZero();
        assertThat(u.getBloqueJusqua()).isNull();
        assertThat(u.estBloque(now)).isFalse();
    }
}
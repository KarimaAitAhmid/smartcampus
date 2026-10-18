package ma.ensa.smartcampus.controller;

import ma.ensa.smartcampus.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * SC-11 — Se déconnecter de SmartCampus.
 * Le backend est sans état (JWT) : la déconnexion renvoie 204
 * et ne modifie rien côté serveur.
 */
class AuthControllerLogoutTest {

    @Test
    void logout_renvoie_204_sans_contenu() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);

        ResponseEntity<Void> reponse = controller.logout();

        assertThat(reponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(reponse.getBody()).isNull();
    }

    @Test
    void logout_ne_sollicite_pas_le_service_d_authentification() {
        AuthService authService = mock(AuthService.class);
        AuthController controller = new AuthController(authService);

        controller.logout();

        verifyNoInteractions(authService);
    }
}
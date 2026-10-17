package ma.ensa.smartcampus.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IdentifiantsInvalidesException.class)
    public ResponseEntity<Map<String, Object>> identifiantsInvalides(IdentifiantsInvalidesException ex) {
        return reponse(HttpStatus.UNAUTHORIZED, "IDENTIFIANTS_INVALIDES", ex.getMessage());
    }

    @ExceptionHandler(CompteBloqueException.class)
    public ResponseEntity<Map<String, Object>> compteBloque(CompteBloqueException ex) {
        return reponse(HttpStatus.LOCKED, "COMPTE_BLOQUE", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + " : " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return reponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERREUR", message);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> accesRefuse(AccessDeniedException ex) {
        return reponse(HttpStatus.FORBIDDEN, "ACCES_REFUSE", "Accès refusé");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> general(Exception ex) {
        return reponse(HttpStatus.INTERNAL_SERVER_ERROR, "ERREUR_INTERNE", "Erreur interne");
    }

    private ResponseEntity<Map<String, Object>> reponse(HttpStatus status, String erreur, String message) {
        return ResponseEntity.status(status).body(Map.of(
                "status", status.value(),
                "error", erreur,
                "message", message));
    }
}
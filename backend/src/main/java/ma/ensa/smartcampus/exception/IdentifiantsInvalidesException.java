package ma.ensa.smartcampus.exception;

public class IdentifiantsInvalidesException
        extends RuntimeException {

    public IdentifiantsInvalidesException() {
        super("E-mail ou mot de passe incorrect");
    }
}
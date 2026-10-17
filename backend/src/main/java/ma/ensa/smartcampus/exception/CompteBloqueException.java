package ma.ensa.smartcampus.exception;

public class CompteBloqueException extends RuntimeException {

    public CompteBloqueException() {
        super("Trop de tentatives, veuillez réessayer plus tard");
    }
}

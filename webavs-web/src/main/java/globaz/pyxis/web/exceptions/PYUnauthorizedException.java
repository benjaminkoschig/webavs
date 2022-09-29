package globaz.pyxis.web.exceptions;

/**
 * Exception due à une erreur technique lors de la récupération du token pour le web service Pyxis
 */
public class PYUnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PYUnauthorizedException(String message) {
        super(message);
    }

    public PYUnauthorizedException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public PYUnauthorizedException(Throwable nestedException) {
        super(nestedException);
    }
}

package globaz.naos.web.exceptions;

/**
 * Exception due � une erreur technique lors de la r�cup�ration du token pour le web service Naos
 */
public class AFUnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AFUnauthorizedException(String message) {
        super(message);
    }

    public AFUnauthorizedException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public AFUnauthorizedException(Throwable nestedException) {
        super(nestedException);
    }
}

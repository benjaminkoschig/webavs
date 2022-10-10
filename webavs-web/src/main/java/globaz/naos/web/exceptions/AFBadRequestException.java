package globaz.naos.web.exceptions;

/**
 * Exception due à une erreur technique dans les paramètres reçu dans la requête web service Naos
 */
public class AFBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AFBadRequestException(String message) {
        super(message);
    }

    public AFBadRequestException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public AFBadRequestException(Throwable nestedException) {
        super(nestedException);
    }
}

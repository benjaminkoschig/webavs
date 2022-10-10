package globaz.pyxis.web.exceptions;

/**
 * Exception due à une erreur technique dans les paramètres reçu dans la requête web service Pyxis
 */
public class PYBadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PYBadRequestException(String message) {
        super(message);
    }

    public PYBadRequestException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public PYBadRequestException(Throwable nestedException) {
        super(nestedException);
    }
}

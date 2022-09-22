package globaz.naos.web.exceptions;

/**
 * Exception due à une erreur interne WebAVS survenue lors de la création de réponse pour une requête Web Services Naos
 */
public class AFInternalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AFInternalException(String message) {
        super(message);
    }

    public AFInternalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public AFInternalException(Throwable nestedException) {
        super(nestedException);
    }
}

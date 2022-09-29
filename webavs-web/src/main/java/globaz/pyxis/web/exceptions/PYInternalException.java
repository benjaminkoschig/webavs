package globaz.pyxis.web.exceptions;

/**
 * Exception due à une erreur interne WebAVS survenue lors de la création de  réponse pour une requête Web Services Pyxis
 */
public class PYInternalException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PYInternalException(String message) {
        super(message);
    }

    public PYInternalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public PYInternalException(Throwable nestedException) {
        super(nestedException);
    }
}

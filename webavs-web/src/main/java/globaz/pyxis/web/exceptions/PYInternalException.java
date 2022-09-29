package globaz.pyxis.web.exceptions;

/**
 * Exception due � une erreur interne WebAVS survenue lors de la cr�ation de  r�ponse pour une requ�te Web Services Pyxis
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

package globaz.corvus.exceptions;

/**
 * Exception due à une erreur technique dans l'application des rentes
 * 
 * @author PBA
 */
public class RETechnicalException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RETechnicalException(String message) {
        super(message);
    }

    public RETechnicalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public RETechnicalException(Throwable nestedException) {
        super(nestedException);
    }
}

package globaz.osiris.eservices.exceptions;

/**
 * Exception due à une erreur technique lors de la récupération du token pour le eServices Ferciam
 */
public class ESUnauthorizedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ESUnauthorizedException(String message) {
        super(message);
    }

    public ESUnauthorizedException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public ESUnauthorizedException(Throwable nestedException) {
        super(nestedException);
    }
}

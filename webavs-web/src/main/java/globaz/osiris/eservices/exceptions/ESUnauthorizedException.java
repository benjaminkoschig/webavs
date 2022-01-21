package globaz.osiris.eservices.exceptions;

/**
 * Exception due � une erreur technique lors de la r�cup�ration du token pour le eServices Ferciam
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

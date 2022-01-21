package globaz.osiris.eservices.exceptions;

/**
 * Exception due à une erreur technique dans les paramètres reçu dans la requête eServices Ferciam
 */
public class ESBadRequestException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ESBadRequestException(String message) {
        super(message);
    }

    public ESBadRequestException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public ESBadRequestException(Throwable nestedException) {
        super(nestedException);
    }
}

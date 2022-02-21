package globaz.osiris.eservices.exceptions;

/**
 * Exception due à une erreur interne WebAVS survenue lors de la création de la réponse pour la requête eServices Ferciam
 */
public class ESInternalException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ESInternalException(String message) {
        super(message);
    }

    public ESInternalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public ESInternalException(Throwable nestedException) {
        super(nestedException);
    }
}

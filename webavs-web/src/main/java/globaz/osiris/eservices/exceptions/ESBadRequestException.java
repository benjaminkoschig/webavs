package globaz.osiris.eservices.exceptions;

/**
 * Exception due � une erreur technique dans les param�tres re�u dans la requ�te eServices Ferciam
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

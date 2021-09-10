package globaz.prestation.acor;

public class PRAcorTechnicalException extends RuntimeException {

    public PRAcorTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public PRAcorTechnicalException(Throwable nestedException) {
        super(nestedException);
    }
}

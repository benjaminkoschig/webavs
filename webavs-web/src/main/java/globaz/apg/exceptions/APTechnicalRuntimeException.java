package globaz.apg.exceptions;

/**
 * Exception relevant d'un probl�me technique dans les APG.<br />
 * Le message peut �tre �crit en anglais pour ce type d'exception.
 */
public class APTechnicalRuntimeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public APTechnicalRuntimeException(String message) {
        super(message);
    }

    public APTechnicalRuntimeException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public APTechnicalRuntimeException(Throwable nestedException) {
        super(nestedException);
    }
}

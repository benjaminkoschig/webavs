package ch.globaz.eform.exception;

public class UnmarshalEFormulaireException extends RuntimeException {
    public UnmarshalEFormulaireException() {
        super();
    }

    public UnmarshalEFormulaireException(String message) {
        super(message);
    }

    public UnmarshalEFormulaireException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnmarshalEFormulaireException(Throwable cause) {
        super(cause);
    }

    protected UnmarshalEFormulaireException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

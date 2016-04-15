package ch.globaz.al.exception;

public class ALGestionnaireException extends Exception {

    private static final long serialVersionUID = 1L;

    public ALGestionnaireException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ALGestionnaireException(String message) {
        super(message);
    }
}

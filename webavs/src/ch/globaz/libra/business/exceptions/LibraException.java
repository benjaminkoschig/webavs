package ch.globaz.libra.business.exceptions;

public class LibraException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public LibraException() {
        super();
    }

    public LibraException(String message) {
        super(message);
    }

    public LibraException(String message, Throwable cause) {
        super(message, cause);
    }

}

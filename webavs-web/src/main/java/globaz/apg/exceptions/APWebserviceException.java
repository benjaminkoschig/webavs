package globaz.apg.exceptions;

public class APWebserviceException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public APWebserviceException(String message) {
        super(message);
    }

    public APWebserviceException(Exception exception) {
        super(exception);
    }

    public APWebserviceException(String message, Exception exception) {
        super(message, exception);
    }
}

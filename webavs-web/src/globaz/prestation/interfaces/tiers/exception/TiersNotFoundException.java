package globaz.prestation.interfaces.tiers.exception;

public class TiersNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    public TiersNotFoundException(String message) {
        super(message);
    }

    public TiersNotFoundException(String message, Throwable parentException) {
        super(message, parentException);
    }
}

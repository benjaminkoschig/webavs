package globaz.prestation.utils;

public class DateException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DateException(String message) {
        super(message);
    }

    public DateException(String message, Exception exception) {
        super(message, exception);
    }
}

package globaz.hercule.exception;

/**
 * @author SCO
 * @since SCO 2 juin 2010
 */
public class HerculeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de HerculeException
     */
    public HerculeException() {
        super();
    }

    /**
     * Constructeur de HerculeException
     */
    public HerculeException(String message) {
        super(message);

    }

    /**
     * Constructeur de HerculeException
     */
    public HerculeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur de HerculeException
     */
    public HerculeException(Throwable cause) {
        super(cause);
    }

}

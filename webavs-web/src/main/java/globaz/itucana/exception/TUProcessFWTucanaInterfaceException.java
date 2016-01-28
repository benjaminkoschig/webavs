package globaz.itucana.exception;

/**
 * Remonte les erreurs du framework (find, count, retrieve,...)
 * 
 * @author fgo date de création : 8 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessFWTucanaInterfaceException extends TUProcessTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TUProcessFWTucanaInterfaceException() {
        super();
    }

    /**
     * @param message
     */
    public TUProcessFWTucanaInterfaceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param subException
     */
    public TUProcessFWTucanaInterfaceException(String message, Throwable subException) {
        super(message, subException);
    }

    /**
     * @param subException
     */
    public TUProcessFWTucanaInterfaceException(Throwable subException) {
        super(subException);
    }

}

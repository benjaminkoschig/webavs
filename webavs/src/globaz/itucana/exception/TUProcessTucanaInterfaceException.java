package globaz.itucana.exception;

/**
 * Classe permettant de remonter toute les erreurs survenues dans les process
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public class TUProcessTucanaInterfaceException extends TUInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUProcessTucanaInterfaceException() {
        super();
    }

    public TUProcessTucanaInterfaceException(String message) {
        super(message);
    }

    public TUProcessTucanaInterfaceException(String message, Throwable subException) {
        super(message, subException);
    }

    public TUProcessTucanaInterfaceException(Throwable subException) {
        super(subException);
    }

}

package globaz.itucana.exception;

/**
 * G�re les exceptions d'initialisation du process interface
 * 
 * @author fgo date de cr�ation : 14 juin 06
 * @version : version 1.0
 * 
 */
public class TUInitProcessTucanaInterfaceException extends TUProcessTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUInitProcessTucanaInterfaceException() {
        super();
    }

    public TUInitProcessTucanaInterfaceException(String message) {
        super(message);
    }

    public TUInitProcessTucanaInterfaceException(String message, Throwable subException) {
        super(message, subException);
    }

    public TUInitProcessTucanaInterfaceException(Throwable subException) {
        super(subException);
    }

}

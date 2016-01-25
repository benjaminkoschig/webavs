package globaz.itucana.exception;

/**
 * Exception lev�e lorsque le service n'est pas trouv�
 * 
 * @author fgo date de cr�ation : 13 juin 06
 * @version : version 1.0
 * 
 */
public class TUNoServiceDefinedException extends TUInitTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TUNoServiceDefinedException() {
        super();
    }

    /**
     * @param message
     */
    public TUNoServiceDefinedException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param subException
     */
    public TUNoServiceDefinedException(String message, Throwable subException) {
        super(message, subException);
    }

    /**
     * @param subException
     */
    public TUNoServiceDefinedException(Throwable subException) {
        super(subException);
    }

}

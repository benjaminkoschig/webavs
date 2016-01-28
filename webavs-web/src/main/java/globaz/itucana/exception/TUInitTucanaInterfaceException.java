package globaz.itucana.exception;

/**
 * Classe d�finissant des erreurs au niveau de l'initialisation de l'interface
 * 
 * @author fgo date de cr�ation : 7 juin 2006
 * @version : version 1.0
 * 
 */
public abstract class TUInitTucanaInterfaceException extends TUInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUInitTucanaInterfaceException() {
        super();
    }

    public TUInitTucanaInterfaceException(String message) {
        super(message);
    }

    public TUInitTucanaInterfaceException(String message, Throwable subException) {
        super(message, subException);
    }

    public TUInitTucanaInterfaceException(Throwable subException) {
        super(subException);
    }

}

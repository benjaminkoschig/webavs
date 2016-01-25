package globaz.itucana.exception;

/**
 * Levée dans le cadre de l'instanciation d'un service si celui-ci ne peut être instancié
 * 
 * @author fgo
 */
public class TUServiceInstanciationException extends TUInitTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUServiceInstanciationException() {
        super();
    }

    public TUServiceInstanciationException(String message) {
        super(message);
    }

    public TUServiceInstanciationException(String message, Throwable subException) {
        super(message, subException);
    }

    public TUServiceInstanciationException(Throwable subException) {
        super(subException);
    }
}

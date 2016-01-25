package globaz.itucana.exception;

/**
 * Classe définisant les erreurs survenues lors de l'instanciation des models
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public class TUModelInstanciationException extends TUInitTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUModelInstanciationException() {
        super();
    }

    public TUModelInstanciationException(String message) {
        super(message);
    }

    public TUModelInstanciationException(String message, Throwable subException) {
        super(message, subException);
    }

    public TUModelInstanciationException(Throwable subException) {
        super(subException);
    }

}

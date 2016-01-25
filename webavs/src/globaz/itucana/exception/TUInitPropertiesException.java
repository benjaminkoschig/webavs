package globaz.itucana.exception;

/**
 * Classe Exception pour gestion des erreurs en rapport avec l'initialisation des properties
 * 
 * @author fgo date de création : 7 juin 2006
 * @version : version 1.0
 * 
 */
public class TUInitPropertiesException extends TUInitTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TUInitPropertiesException() {
        super();
    }

    public TUInitPropertiesException(String message) {
        super(message);
    }

    public TUInitPropertiesException(String message, Throwable subException) {
        super(message, subException);
    }

    public TUInitPropertiesException(Throwable subException) {
        super(subException);
    }
}

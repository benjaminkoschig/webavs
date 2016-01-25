package globaz.itucana.exception;

/**
 * Erreur soulevée si l'enregistrement bouclement est inexistant. En fait l'enregistrement bouclement doit forcément
 * existé, car il est créé lors de l'importation du bouclement précédent, car il faut avoir le solde du bouclement
 * précédent
 * 
 * @author fgo
 * 
 *         date de création : 8 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessNoBouclementTucanaInterfaceException extends TUProcessTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TUProcessNoBouclementTucanaInterfaceException() {
        super();
    }

    /**
     * @param message
     */
    public TUProcessNoBouclementTucanaInterfaceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param subException
     */
    public TUProcessNoBouclementTucanaInterfaceException(String message, Throwable subException) {
        super(message, subException);
    }

    /**
     * @param subException
     */
    public TUProcessNoBouclementTucanaInterfaceException(Throwable subException) {
        super(subException);
    }

}

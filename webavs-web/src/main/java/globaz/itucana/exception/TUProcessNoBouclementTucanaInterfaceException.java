package globaz.itucana.exception;

/**
 * Erreur soulev�e si l'enregistrement bouclement est inexistant. En fait l'enregistrement bouclement doit forc�ment
 * exist�, car il est cr�� lors de l'importation du bouclement pr�c�dent, car il faut avoir le solde du bouclement
 * pr�c�dent
 * 
 * @author fgo
 * 
 *         date de cr�ation : 8 juin 06
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

package globaz.itucana.exception;

/**
 * Erreur lev�e lors d'un probl�me sur le chargement des codes syst�me rubrique (TU_RUBR)
 * 
 * @author fgo date de cr�ation : 8 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessCSTucanaInterfaceException extends TUProcessTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TUProcessCSTucanaInterfaceException() {
        super();
    }

    /**
     * @param message
     */
    public TUProcessCSTucanaInterfaceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param subException
     */
    public TUProcessCSTucanaInterfaceException(String message, Throwable subException) {
        super(message, subException);
    }

    /**
     * @param subException
     */
    public TUProcessCSTucanaInterfaceException(Throwable subException) {
        super(subException);
    }

}

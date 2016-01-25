package globaz.itucana.exception;

/**
 * Remonte une erreur en cas d'existance de l'ent�te pour un application, une ann�e et un mois donn�
 * 
 * @author fgo date de cr�ation : 8 juin 06
 * @version : version 1.0
 * 
 */
public class TUProcessEnteteExistTucanaInterfaceException extends TUProcessTucanaInterfaceException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public TUProcessEnteteExistTucanaInterfaceException() {
        super();
    }

    /**
     * @param message
     */
    public TUProcessEnteteExistTucanaInterfaceException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param subException
     */
    public TUProcessEnteteExistTucanaInterfaceException(String message, Throwable subException) {
        super(message, subException);
    }

    /**
     * @param subException
     */
    public TUProcessEnteteExistTucanaInterfaceException(Throwable subException) {
        super(subException);
    }

}

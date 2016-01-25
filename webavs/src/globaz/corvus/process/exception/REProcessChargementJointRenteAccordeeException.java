/**
 * 
 */
package globaz.corvus.process.exception;

/**
 * Erreur lev�e en cas de probl�me de chargement du manager des renteAccordee et renteRetenue
 * 
 * @author FGO
 * 
 */
public class REProcessChargementJointRenteAccordeeException extends REProcessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public REProcessChargementJointRenteAccordeeException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public REProcessChargementJointRenteAccordeeException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public REProcessChargementJointRenteAccordeeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public REProcessChargementJointRenteAccordeeException(Throwable cause) {
        super(cause);
    }

}

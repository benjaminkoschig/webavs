/**
 * 
 */
package globaz.corvus.process.exception;

/**
 * Exception permettant de lever une exception quand problème au niveau du chargement de l'enregsitrement renteAccordee
 * 
 * @author FGO
 * 
 */
public class REProcessChargementRenteAccordeeException extends REProcessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public REProcessChargementRenteAccordeeException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public REProcessChargementRenteAccordeeException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public REProcessChargementRenteAccordeeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public REProcessChargementRenteAccordeeException(Throwable cause) {
        super(cause);
    }

}

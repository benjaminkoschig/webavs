/**
 * 
 */
package globaz.hera.impl.ijai;

/**
 * Exception levée en cas de pe
 * 
 * @author FGO
 * 
 */
public class IJAIUndefinedTypePeriodeException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public IJAIUndefinedTypePeriodeException() {
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public IJAIUndefinedTypePeriodeException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public IJAIUndefinedTypePeriodeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public IJAIUndefinedTypePeriodeException(Throwable cause) {
        super(cause);
    }

}

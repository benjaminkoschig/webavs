/**
 * Exception mère des exceptions levées dans les process
 */
package globaz.corvus.process.exception;

/**
 * @author FGO
 * 
 */
public class REProcessException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public REProcessException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public REProcessException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public REProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public REProcessException(Throwable cause) {
        super(cause);
    }
}

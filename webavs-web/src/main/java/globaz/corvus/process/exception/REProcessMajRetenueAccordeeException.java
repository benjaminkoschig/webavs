/**
 * Exception lev�e en cas de probl�me au niveau de la mise � jour d'une rente accord�e
 */
package globaz.corvus.process.exception;

/**
 * @author FGO
 * 
 */
public class REProcessMajRetenueAccordeeException extends REProcessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public REProcessMajRetenueAccordeeException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public REProcessMajRetenueAccordeeException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public REProcessMajRetenueAccordeeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public REProcessMajRetenueAccordeeException(Throwable cause) {
        super(cause);
    }
}

package globaz.corvus.process.exception;

/**
 * Exception lev�e en cas de probl�me lors du chargement de la r�cap mensuelle
 * 
 * @author FGO
 * 
 */
public class REProcessLoadRecapMensuelleException extends REProcessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public REProcessLoadRecapMensuelleException() {
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public REProcessLoadRecapMensuelleException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public REProcessLoadRecapMensuelleException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public REProcessLoadRecapMensuelleException(Throwable cause) {
        super(cause);
    }
}

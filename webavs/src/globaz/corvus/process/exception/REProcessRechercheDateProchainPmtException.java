package globaz.corvus.process.exception;

/**
 * Lève l'exeption si un problème survient lors de la recherche de la date du prochain paiment
 * 
 * @author FGO
 * 
 */
public class REProcessRechercheDateProchainPmtException extends REProcessException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur
     */
    public REProcessRechercheDateProchainPmtException() {
        super();
    }

    /**
     * Constructeur
     * 
     * @param message
     */
    public REProcessRechercheDateProchainPmtException(String message) {
        super(message);
    }

    /**
     * Constructeur
     * 
     * @param message
     * @param cause
     */
    public REProcessRechercheDateProchainPmtException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructeur
     * 
     * @param cause
     */
    public REProcessRechercheDateProchainPmtException(Throwable cause) {
        super(cause);
    }
}

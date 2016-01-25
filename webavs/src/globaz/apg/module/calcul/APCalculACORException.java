package globaz.apg.module.calcul;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Exception lancée si un calcul est lancé et qu'il devrait normalement être exécuté au moyen d'ACOR.
 * </p>
 * 
 * @author vre
 */
public class APCalculACORException extends APCalculException {

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crée une nouvelle instance de la classe APCalculACORException.
     */
    public APCalculACORException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe APCalculACORException.
     * 
     * @param msg
     *            DOCUMENT ME!
     */
    public APCalculACORException(String msg) {
        super(msg);
    }

    /**
     * Crée une nouvelle instance de la classe APCalculACORException.
     * 
     * @param msg
     *            DOCUMENT ME!
     * @param cause
     *            DOCUMENT ME!
     */
    public APCalculACORException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

package globaz.apg.module.calcul;

/**
 * <H1>Description</H1>
 * 
 * <p>
 * Exception lanc�e si un calcul est lanc� et qu'il devrait normalement �tre ex�cut� au moyen d'ACOR.
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
     * Cr�e une nouvelle instance de la classe APCalculACORException.
     */
    public APCalculACORException() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe APCalculACORException.
     * 
     * @param msg
     *            DOCUMENT ME!
     */
    public APCalculACORException(String msg) {
        super(msg);
    }

    /**
     * Cr�e une nouvelle instance de la classe APCalculACORException.
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

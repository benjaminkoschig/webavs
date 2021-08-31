package globaz.prestation.acor;

/**
 * Repr�sente les exceptions m�tiers en relation avec les web services ACOR.
 */
public class PRAcorDomaineException extends RuntimeException {
    /**
     * Cr�e une nouvelle instance de la classe PRAcorDomaineException.
     */
    public PRAcorDomaineException() {
        super();
    }

    /**
     * Cr�e une nouvelle instance de la classe PRAcorDomaineException.
     *
     * @param msg : message de l'exception
     */
    public PRAcorDomaineException(String msg) {
        super(msg);
    }
}

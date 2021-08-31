package globaz.prestation.acor;

/**
 * Représente les exceptions métiers en relation avec les web services ACOR.
 */
public class PRAcorDomaineException extends RuntimeException {
    /**
     * Crée une nouvelle instance de la classe PRAcorDomaineException.
     */
    public PRAcorDomaineException() {
        super();
    }

    /**
     * Crée une nouvelle instance de la classe PRAcorDomaineException.
     *
     * @param msg : message de l'exception
     */
    public PRAcorDomaineException(String msg) {
        super(msg);
    }
}

package ch.globaz.orion.business.exceptions;

/**
 * 
 * @author sco
 * @since 12 avr. 2011
 */
public class OrionDanException extends OrionException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public OrionDanException() {
        super();
    }

    /**
     * @param message
     *            Message spécifiant le context de l'exception
     */
    public OrionDanException(String m) {
        super(m);
    }

    /**
     * @param message
     *            Message spécifiant le context de l'exception
     */
    public OrionDanException(String m, Throwable t) {
        super(m, t);
    }

}

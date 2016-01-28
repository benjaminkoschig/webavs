package ch.globaz.perseus.business.exceptions.models.qd;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * Gestion des exceptions d'une QD.
 * 
 * @author JSI
 * 
 */
public class QDException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public QDException() {
        super();
    }

    public QDException(String m) {
        super(m);
    }

    public QDException(String m, Throwable t) {
        super(m, t);
    }

}

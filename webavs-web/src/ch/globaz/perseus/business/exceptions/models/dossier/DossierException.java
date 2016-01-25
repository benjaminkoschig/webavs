package ch.globaz.perseus.business.exceptions.models.dossier;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class DossierException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public DossierException() {
        super();
    }

    /**
     * @param m
     */
    public DossierException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public DossierException(String m, Throwable t) {
        super(m, t);
    }

}

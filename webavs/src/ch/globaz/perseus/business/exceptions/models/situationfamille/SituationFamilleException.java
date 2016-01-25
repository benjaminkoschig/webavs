package ch.globaz.perseus.business.exceptions.models.situationfamille;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * TODO Javadoc
 * 
 * @author vyj
 */
public class SituationFamilleException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public SituationFamilleException() {
        super();
    }

    /**
     * @param m
     */
    public SituationFamilleException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public SituationFamilleException(String m, Throwable t) {
        super(m, t);
    }

}

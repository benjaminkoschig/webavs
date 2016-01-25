package ch.globaz.perseus.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception mère des exceptions générées par l'application de gestion des prestations complémentaires famille
 * 
 * @author vyj
 */
public class PerseusException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PerseusException() {
        super();
    }

    /**
     * @param m
     */
    public PerseusException(String m) {
        super(m);
    }

    /**
     * @param m
     * @param t
     */
    public PerseusException(String m, Throwable t) {
        super(m, t);
    }

}

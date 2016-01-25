package ch.globaz.perseus.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception m�re des exceptions g�n�r�es par l'application de gestion des prestations compl�mentaires famille
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

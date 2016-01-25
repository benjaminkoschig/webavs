package ch.globaz.cygnus.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Classe m�re de toutes les exceptions m�tiers
 * 
 * @author LFO
 */
public abstract class CygnusException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CygnusException() {
        super();
    }

    public CygnusException(String m) {
        super(m);
    }

    public CygnusException(String m, Throwable t) {
        super(m, t);
    }

}

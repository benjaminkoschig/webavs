package ch.globaz.musca.business.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Classe <code>Exception</code> mère de toutes les exceptions générées par le métier de l'application facturation
 * 
 * @author GMO
 */
public abstract class FAException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FAException() {
        super();

    }

    public FAException(String m) {
        super(m);

    }

    public FAException(String m, Throwable t) {
        super(m, t);

    }

}

package ch.globaz.musca.business.exceptions.models;

import ch.globaz.musca.business.exceptions.FAException;

/**
 * Classe <code>Exception</code> métier passage facturation
 * 
 */
public class PassageModelException extends FAException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PassageModelException() {
        super();

    }

    public PassageModelException(String m) {
        super(m);

    }

    public PassageModelException(String m, Throwable t) {
        super(m, t);

    }

}

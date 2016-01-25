package ch.globaz.al.business.exceptions.languesAllocAffilie;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour la gestion de la langue de l'allocataire ou de l'affilié
 * 
 * @author PTA
 * 
 */

public class ALLangueAllocAffilieException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALLangueAllocAffilieException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALLangueAllocAffilieException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALLangueAllocAffilieException(String m, Throwable t) {
        super(m, t);

    }

}

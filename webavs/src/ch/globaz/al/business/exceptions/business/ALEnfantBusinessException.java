package ch.globaz.al.business.exceptions.business;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les services business liés aux enfants
 * 
 * @author jts
 */
public class ALEnfantBusinessException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALEnfantBusinessException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALEnfantBusinessException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALEnfantBusinessException(String m, Throwable t) {
        super(m, t);
    }
}

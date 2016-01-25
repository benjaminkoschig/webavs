package ch.globaz.al.business.exceptions.affiliations;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les services business liés aux affiliations
 * 
 * @author jts
 */
public class ALAffiliationException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAffiliationException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAffiliationException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAffiliationException(String m, Throwable t) {
        super(m, t);
    }
}

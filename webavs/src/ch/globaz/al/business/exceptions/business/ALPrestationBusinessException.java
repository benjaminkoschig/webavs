package ch.globaz.al.business.exceptions.business;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les services business liés aux prestations
 * 
 * @author jts
 */
public class ALPrestationBusinessException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALPrestationBusinessException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALPrestationBusinessException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALPrestationBusinessException(String m, Throwable t) {
        super(m, t);
    }
}

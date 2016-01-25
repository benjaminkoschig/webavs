package ch.globaz.al.business.exceptions.importation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> levée si plusieurs personnes ont été trouvée lors de l'importation
 * 
 * @author jts
 */
public class ALSeveralPersonnesFoundException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALSeveralPersonnesFoundException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALSeveralPersonnesFoundException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALSeveralPersonnesFoundException(String m, Throwable t) {
        super(m, t);
    }
}
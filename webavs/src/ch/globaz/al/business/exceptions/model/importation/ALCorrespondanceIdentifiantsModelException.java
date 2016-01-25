package ch.globaz.al.business.exceptions.model.importation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour
 * 
 * @author jts
 */
public class ALCorrespondanceIdentifiantsModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCorrespondanceIdentifiantsModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCorrespondanceIdentifiantsModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCorrespondanceIdentifiantsModelException(String m, Throwable t) {
        super(m, t);
    }

}
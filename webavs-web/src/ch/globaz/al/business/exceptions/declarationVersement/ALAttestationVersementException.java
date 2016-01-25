/**
 * 
 */
package ch.globaz.al.business.exceptions.declarationVersement;

import ch.globaz.al.business.exceptions.ALException;

/**
 * @author pta
 * 
 */
public class ALAttestationVersementException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALAttestationVersementException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALAttestationVersementException(String m) {
        super(m);

    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALAttestationVersementException(String m, Throwable t) {
        super(m, t);

    }

}

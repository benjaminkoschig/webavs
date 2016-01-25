package ch.globaz.ij.businessimpl.exception;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class TechnicalException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Message doesn't will be internationalized for technical exception
     * 
     * @param message
     *            The message
     */
    public TechnicalException(String message) {
        super(message);
    }
}

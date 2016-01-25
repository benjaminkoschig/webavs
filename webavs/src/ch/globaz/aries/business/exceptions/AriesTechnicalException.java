package ch.globaz.aries.business.exceptions;

import ch.globaz.common.business.exceptions.CommonTechnicalException;

/**
 * Exception pour les erreurs techniques uniquement
 * 
 * @author mmo
 * 
 */
public class AriesTechnicalException extends CommonTechnicalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     *            Message de context
     */
    public AriesTechnicalException(String message) {
        super(message);
    }

    /**
     * @param message
     *            Message de context
     * @param cause
     *            Exception catchée et encapsulée
     */
    public AriesTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

}

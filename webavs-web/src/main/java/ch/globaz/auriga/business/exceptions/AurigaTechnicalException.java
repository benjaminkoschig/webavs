package ch.globaz.auriga.business.exceptions;

import ch.globaz.common.exceptions.CommonTechnicalException;

/**
 * Exception pour les erreurs techniques uniquement
 * 
 * @author bjo
 * 
 */
public class AurigaTechnicalException extends CommonTechnicalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     *            Message de context
     */
    public AurigaTechnicalException(String message) {
        super(message);
    }

    /**
     * @param message
     *            Message de context
     * @param cause
     *            Exception catchée et encapsulée
     */
    public AurigaTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

}

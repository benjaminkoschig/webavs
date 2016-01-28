package ch.globaz.auriga.business.exceptions;

import ch.globaz.common.business.exceptions.CommonJobException;

/**
 * Exceptions métier pour l'application Auriga
 * 
 * @author bjo
 * 
 */
public class AurigaException extends CommonJobException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AurigaException(String messageId) {
        super(messageId);

    }

    public AurigaException(String messageId, String... parameters) {
        super(messageId, parameters);

    }

}

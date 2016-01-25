package ch.globaz.aries.business.exceptions;

import ch.globaz.common.business.exceptions.CommonJobException;

/**
 * Exceptions métier pour l'application Aries
 * 
 * @author mmo
 * 
 */
public class AriesException extends CommonJobException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AriesException(String messageId) {
        super(messageId);
    }

    public AriesException(String messageId, String... parameters) {
        super(messageId, parameters);
    }

}

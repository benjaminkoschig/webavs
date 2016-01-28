package ch.globaz.common.business.exceptions;

import globaz.jade.context.JadeThread;

/**
 * Exception racine des exceptions métiers
 * 
 * Les Exceptions métier de chaque module doivent étendre cette classe
 * 
 * @author MMO
 */
public class CommonJobException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CommonJobException(String messageId) {
        super(JadeThread.getMessage(messageId));
    }

    public CommonJobException(String messageId, String... parameters) {
        super(JadeThread.getMessage(messageId, parameters));
    }
}

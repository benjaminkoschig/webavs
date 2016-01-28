package ch.globaz.common.business.exceptions;

import globaz.jade.context.JadeThread;

/**
 * Exception racine des exceptions m�tiers
 * 
 * Les Exceptions m�tier de chaque module doivent �tendre cette classe
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

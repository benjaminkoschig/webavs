package ch.globaz.ij.businessimpl.exception;

import globaz.jade.context.JadeThread;
import ch.globaz.corvus.business.exceptions.CorvusException;

public class BusinessException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Message will be internationalized
     * 
     * @param message
     *            The message key
     */
    public BusinessException(String message) {
        super(JadeThread.getMessage(message));
    }

    /**
     * Message will be internationalized
     * 
     * @param message
     *            The message key
     * @param params
     *            Associated parameters for the message
     */
    public BusinessException(String message, String[] params) {
        super(JadeThread.getMessage(message, params));
    }

}

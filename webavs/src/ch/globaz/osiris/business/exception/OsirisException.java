package ch.globaz.osiris.business.exception;

import globaz.jade.exception.JadeApplicationException;

/**
 * @author SCO 18 mai 2010
 */
public class OsirisException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructeur de OsirisException
     */
    public OsirisException(String message) {
        super(message);
    }

    /**
     * Constructeur de OsirisException
     */
    public OsirisException(String msg, Exception e) {
        super(msg, e);
    }
}

package globaz.osiris.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception relevant d'un problème du domaine technique dans la comptabilité auxiliaire.<br />
 * Le message peut être écrit en anglais pour ce type d'exception.
 * 
 * @author PBA
 */
public class CATechnicalException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CATechnicalException() {
        super();
    }

    public CATechnicalException(String message) {
        super(message);
    }

    public CATechnicalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }
}

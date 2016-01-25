package globaz.osiris.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception relevant d'un probl�me du domaine technique dans la comptabilit� auxiliaire.<br />
 * Le message peut �tre �crit en anglais pour ce type d'exception.
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

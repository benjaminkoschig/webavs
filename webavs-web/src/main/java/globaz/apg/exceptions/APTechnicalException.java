package globaz.apg.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception relevant d'un probl�me technique dans les APG.<br />
 * Le message peut �tre �crit en anglais pour ce type d'exception.
 * 
 * @author PBA
 */
public class APTechnicalException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APTechnicalException() {
        super();
    }

    public APTechnicalException(String message) {
        super(message);
    }

    public APTechnicalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }
}

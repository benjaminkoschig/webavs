package globaz.osiris.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception relevant d'un probl�me m�tier dans la comptabilit� auxiliaire
 * 
 * @author PBA
 */
public class CABusinessException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public CABusinessException() {
        super();
    }

    public CABusinessException(String message) {
        super(message);
    }

    public CABusinessException(String message, Throwable nestedException) {
        super(message, nestedException);
    }
}

package globaz.apg.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception relevant d'un problème métier dans les APG.
 * 
 * @author PBA
 */
public class APBusinessException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public APBusinessException() {
        super();
    }

    public APBusinessException(String message) {
        super(message);
    }

    public APBusinessException(String message, Throwable nestedException) {
        super(message, nestedException);
    }
}

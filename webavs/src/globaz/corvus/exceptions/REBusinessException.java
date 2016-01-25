package globaz.corvus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception due à une erreur métier dans l'application des rentes
 * 
 * @author PBA
 */
public class REBusinessException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public REBusinessException(String message) {
        super(message);
    }
}

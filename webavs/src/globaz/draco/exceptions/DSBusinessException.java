package globaz.draco.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception due à une erreur Metier dans le module de l'affiliation
 * 
 * @author sco
 */
public class DSBusinessException extends JadeApplicationException {

    private static final long serialVersionUID = 1756885994993919472L;

    public DSBusinessException() {
        super();
    }

    public DSBusinessException(String message) {
        super(message);
    }

    public DSBusinessException(String message, Throwable nestedException) {
        super(message, nestedException);
    }
}
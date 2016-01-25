package globaz.naos.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception due à une erreur Metier dans le module de l'affiliation
 * 
 * @author sco
 */
public class AFBusinessException extends JadeApplicationException {

    private static final long serialVersionUID = 1756885994993919472L;

    public AFBusinessException() {
        super();
    }

    public AFBusinessException(String message) {
        super(message);
    }

    public AFBusinessException(String message, Throwable nestedException) {
        super(message, nestedException);
    }
}
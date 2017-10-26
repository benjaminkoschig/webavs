package globaz.draco.exceptions;

import ch.globaz.common.exceptions.CommonTechnicalException;

/**
 * Exception due à une erreur technique dans le module de l'affiliation
 * 
 * @author sco
 */
public class DSTechnicalException extends CommonTechnicalException {

    private static final long serialVersionUID = 134356177984019781L;

    public DSTechnicalException(String message) {
        super(message);
    }

    public DSTechnicalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public DSTechnicalException(Throwable nestedException) {
        super(nestedException);
    }
}

package globaz.naos.exceptions;

import ch.globaz.common.business.exceptions.CommonTechnicalException;

/**
 * Exception due à une erreur technique dans le module de l'affiliation
 * 
 * @author sco
 */
public class AFTechnicalException extends CommonTechnicalException {

    private static final long serialVersionUID = 134356177984019781L;

    public AFTechnicalException(String message) {
        super(message);
    }

    public AFTechnicalException(String message, Throwable nestedException) {
        super(message, nestedException);
    }

    public AFTechnicalException(Throwable nestedException) {
        super(nestedException);
    }
}

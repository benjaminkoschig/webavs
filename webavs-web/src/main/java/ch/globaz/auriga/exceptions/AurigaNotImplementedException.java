package ch.globaz.auriga.exceptions;

import ch.globaz.auriga.business.exceptions.AurigaTechnicalException;

/**
 * Exceptions à lever pour indiquer qu'une fonctionnalité n'est pas implémentée
 * 
 * @author bjo
 * 
 */
public class AurigaNotImplementedException extends AurigaTechnicalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AurigaNotImplementedException() {
        super("Not implemented");
    }
}

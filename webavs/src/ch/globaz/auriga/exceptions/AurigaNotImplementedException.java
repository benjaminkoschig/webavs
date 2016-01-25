package ch.globaz.auriga.exceptions;

import ch.globaz.auriga.business.exceptions.AurigaTechnicalException;

/**
 * Exceptions � lever pour indiquer qu'une fonctionnalit� n'est pas impl�ment�e
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

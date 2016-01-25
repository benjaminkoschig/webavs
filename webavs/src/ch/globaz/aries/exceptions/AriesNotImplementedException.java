package ch.globaz.aries.exceptions;

import ch.globaz.aries.business.exceptions.AriesTechnicalException;

/**
 * Exceptions � lever pour indiquer qu'une fonctionnalit� n'est pas impl�ment�e
 * 
 * @author mmo
 * 
 */
public class AriesNotImplementedException extends AriesTechnicalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AriesNotImplementedException() {
        super("Not implemented");
    }
}

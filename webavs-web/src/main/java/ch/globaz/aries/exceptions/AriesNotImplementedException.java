package ch.globaz.aries.exceptions;

import ch.globaz.aries.business.exceptions.AriesTechnicalException;

/**
 * Exceptions à lever pour indiquer qu'une fonctionnalité n'est pas implémentée
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

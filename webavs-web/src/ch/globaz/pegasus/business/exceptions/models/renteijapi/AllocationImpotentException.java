package ch.globaz.pegasus.business.exceptions.models.renteijapi;

import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * Classe de gestion des exception pour les Allocations Impotents AI 6.2010
 * 
 * @author SCE
 * 
 */
public class AllocationImpotentException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AllocationImpotentException() {
        super();
    }

    public AllocationImpotentException(String str) {
        super(str);
    }

    public AllocationImpotentException(String str, Throwable throwable) {
        super(str, throwable);
    }
}

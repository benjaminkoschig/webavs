package ch.globaz.pegasus.business.exceptions.models.renteijapi;

import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * Classe de gestion des exception pour les membres familles des allocations impotents AI 6.2010
 * 
 * @author JJE
 * 
 */
public class MembreFamilleAllocationImpotentException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MembreFamilleAllocationImpotentException() {
        super();
    }

    public MembreFamilleAllocationImpotentException(String str) {
        super(str);
    }

    public MembreFamilleAllocationImpotentException(String str, Throwable throwable) {
        super(str, throwable);
    }
}

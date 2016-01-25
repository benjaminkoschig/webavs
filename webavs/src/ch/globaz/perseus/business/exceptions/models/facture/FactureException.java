package ch.globaz.perseus.business.exceptions.models.facture;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * Gestion des exceptions liées aux factures.
 * 
 * @author JSI
 * 
 */
public class FactureException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FactureException() {
        super();
    }

    public FactureException(String m) {
        super(m);
    }

    public FactureException(String m, Throwable t) {
        super(m, t);
    }

}

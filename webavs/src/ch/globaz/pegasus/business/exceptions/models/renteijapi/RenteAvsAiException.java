package ch.globaz.pegasus.business.exceptions.models.renteijapi;

import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * Classe de gestion des exceptions pour les Rentes AVS/AI
 * 
 * @author SCE
 * 
 */
public class RenteAvsAiException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RenteAvsAiException() {
        super();
    }

    public RenteAvsAiException(String m) {
        super(m);
    }

    public RenteAvsAiException(String m, Throwable t) {
        super(m, t);
    }
}

package ch.globaz.pegasus.business.exceptions.models.demande;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DemandeException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DemandeException() {
    }

    public DemandeException(String m) {
        super(m);
    }

    public DemandeException(String m, Throwable t) {
        super(m, t);
    }

}

package ch.globaz.pegasus.business.exceptions.models.process;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class RenteAdapationDemandeException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RenteAdapationDemandeException() {
        super();
    }

    public RenteAdapationDemandeException(String m) {
        super(m);
    }

    public RenteAdapationDemandeException(String m, Throwable t) {
        super(m, t);
    }
}

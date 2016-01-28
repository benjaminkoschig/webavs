package ch.globaz.pegasus.business.exceptions.models.dossiers;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DossierException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DossierException() {
        super();
    }

    public DossierException(String m) {
        super(m);
    }

    public DossierException(String m, Throwable t) {
        super(m, t);
    }

}

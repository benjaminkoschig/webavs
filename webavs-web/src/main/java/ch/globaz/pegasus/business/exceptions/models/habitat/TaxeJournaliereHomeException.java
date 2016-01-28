package ch.globaz.pegasus.business.exceptions.models.habitat;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class TaxeJournaliereHomeException extends PegasusException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TaxeJournaliereHomeException() {
    }

    public TaxeJournaliereHomeException(String m) {
        super(m);
    }

    public TaxeJournaliereHomeException(String m, Throwable t) {
        super(m, t);
    }

}

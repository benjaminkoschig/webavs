package ch.globaz.pegasus.business.exceptions.models.home;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class MembreFamilleHomeException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MembreFamilleHomeException() {
        super();
    }

    public MembreFamilleHomeException(String m) {
        super(m);
    }

    public MembreFamilleHomeException(String m, Throwable t) {
        super(m, t);
    }

}

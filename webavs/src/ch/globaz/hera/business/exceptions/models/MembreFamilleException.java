package ch.globaz.hera.business.exceptions.models;

import ch.globaz.hera.business.exceptions.HeraException;

public class MembreFamilleException extends HeraException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public MembreFamilleException() {
    }

    public MembreFamilleException(String m) {
        super(m);
    }

    public MembreFamilleException(String m, Throwable t) {
        super(m, t);
    }

}

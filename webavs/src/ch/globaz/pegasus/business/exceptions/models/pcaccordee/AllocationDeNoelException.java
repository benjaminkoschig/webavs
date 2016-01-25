package ch.globaz.pegasus.business.exceptions.models.pcaccordee;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AllocationDeNoelException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AllocationDeNoelException() {
        super();
    }

    public AllocationDeNoelException(String m) {
        super(m);
    }

    public AllocationDeNoelException(String m, Throwable t) {
        super(m, t);
    }
}

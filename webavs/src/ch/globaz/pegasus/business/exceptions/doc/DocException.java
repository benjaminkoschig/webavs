package ch.globaz.pegasus.business.exceptions.doc;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DocException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DocException() {
        super();
    }

    public DocException(String m) {
        super(m);
    }

    public DocException(String m, Throwable t) {
        super(m, t);
    }
}

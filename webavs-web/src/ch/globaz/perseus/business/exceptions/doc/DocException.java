package ch.globaz.perseus.business.exceptions.doc;

import ch.globaz.perseus.business.exceptions.PerseusException;

public class DocException extends PerseusException {

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

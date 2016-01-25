package ch.globaz.amal.business.exceptions.models.documents;

import ch.globaz.amal.business.exceptions.AmalException;

public class DocumentException extends AmalException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DocumentException() {
        super();
    }

    public DocumentException(String m) {
        super(m);
    }

    public DocumentException(String m, Throwable t) {
        super(m, t);
    }
}

package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * author jje
 */
public class RFImportationTmrException extends JadeApplicationException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public RFImportationTmrException() {
        super();
    }

    public RFImportationTmrException(String m) {
        super(m);
    }

    public RFImportationTmrException(String m, Throwable t) {
        super(m, t);
    }

}

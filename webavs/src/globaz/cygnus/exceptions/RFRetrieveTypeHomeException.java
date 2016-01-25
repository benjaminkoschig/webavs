package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * author jje
 */
public class RFRetrieveTypeHomeException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFRetrieveTypeHomeException() {
        super();
    }

    public RFRetrieveTypeHomeException(String m) {
        super(m);
    }

    public RFRetrieveTypeHomeException(String m, Throwable t) {
        super(m, t);
    }

}

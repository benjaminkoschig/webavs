package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * author jje
 */
public class RFRetrieveApiException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFRetrieveApiException() {
        super();
    }

    public RFRetrieveApiException(String m) {
        super(m);
    }

    public RFRetrieveApiException(String m, Throwable t) {
        super(m, t);
    }

}

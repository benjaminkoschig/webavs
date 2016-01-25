package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * author jje
 */
public class RFRetrieveRentesIjApiException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFRetrieveRentesIjApiException() {
        super();
    }

    public RFRetrieveRentesIjApiException(String m) {
        super(m);
    }

    public RFRetrieveRentesIjApiException(String m, Throwable t) {
        super(m, t);
    }

}

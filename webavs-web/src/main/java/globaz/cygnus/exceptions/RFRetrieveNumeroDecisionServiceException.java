package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * author jje
 */
public class RFRetrieveNumeroDecisionServiceException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFRetrieveNumeroDecisionServiceException() {
        super();
    }

    public RFRetrieveNumeroDecisionServiceException(String m) {
        super(m);
    }

    public RFRetrieveNumeroDecisionServiceException(String m, Throwable t) {
        super(m, t);
    }

}

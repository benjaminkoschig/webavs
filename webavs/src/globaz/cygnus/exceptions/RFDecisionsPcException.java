package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * author jje
 */
public class RFDecisionsPcException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFDecisionsPcException() {
        super();
    }

    public RFDecisionsPcException(String m) {
        super(m);
    }

    public RFDecisionsPcException(String m, Throwable t) {
        super(m, t);
    }

}

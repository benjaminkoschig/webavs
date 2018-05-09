package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * author jje
 */
public class RFRetrieveIsEnfantException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFRetrieveIsEnfantException() {
        super();
    }

    public RFRetrieveIsEnfantException(String m) {
        super(m);
    }

    public RFRetrieveIsEnfantException(String m, Throwable t) {
        super(m, t);
    }

}

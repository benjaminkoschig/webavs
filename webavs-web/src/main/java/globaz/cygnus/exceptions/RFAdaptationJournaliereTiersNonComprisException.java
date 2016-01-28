package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

public class RFAdaptationJournaliereTiersNonComprisException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFAdaptationJournaliereTiersNonComprisException() {
        super();
    }

    public RFAdaptationJournaliereTiersNonComprisException(String m) {
        super(m);
    }

    public RFAdaptationJournaliereTiersNonComprisException(String m, Throwable t) {
        super(m, t);
    }
}
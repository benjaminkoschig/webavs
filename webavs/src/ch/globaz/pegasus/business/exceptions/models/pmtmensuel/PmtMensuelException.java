package ch.globaz.pegasus.business.exceptions.models.pmtmensuel;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PmtMensuelException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PmtMensuelException() {
        super();
    }

    public PmtMensuelException(String m) {
        super(m);
    }

    public PmtMensuelException(String m, Throwable t) {
        super(m, t);
    }
}

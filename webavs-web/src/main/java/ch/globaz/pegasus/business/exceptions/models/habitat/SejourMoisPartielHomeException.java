package ch.globaz.pegasus.business.exceptions.models.habitat;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class SejourMoisPartielHomeException extends PegasusException {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SejourMoisPartielHomeException() {
    }

    public SejourMoisPartielHomeException(String m) {
        super(m);
    }

    public SejourMoisPartielHomeException(String m, Throwable t) {
        super(m, t);
    }

}

package ch.globaz.pegasus.business.exceptions.models.home;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class ChambreMedicaliseeException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ChambreMedicaliseeException() {
        super();
    }

    public ChambreMedicaliseeException(String m) {
        super(m);
    }

    public ChambreMedicaliseeException(String m, Throwable t) {
        super(m, t);
    }

}

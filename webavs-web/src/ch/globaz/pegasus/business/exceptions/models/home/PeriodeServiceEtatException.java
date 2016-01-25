package ch.globaz.pegasus.business.exceptions.models.home;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PeriodeServiceEtatException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public PeriodeServiceEtatException() {
        super();
    }

    public PeriodeServiceEtatException(String m) {
        super(m);
    }

    public PeriodeServiceEtatException(String m, Throwable t) {
        super(m, t);
    }

}

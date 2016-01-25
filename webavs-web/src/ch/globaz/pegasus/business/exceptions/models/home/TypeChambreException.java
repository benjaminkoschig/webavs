package ch.globaz.pegasus.business.exceptions.models.home;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class TypeChambreException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TypeChambreException() {
        super();
    }

    public TypeChambreException(String m) {
        super(m);
    }

    public TypeChambreException(String m, Throwable t) {
        super(m, t);
    }

}

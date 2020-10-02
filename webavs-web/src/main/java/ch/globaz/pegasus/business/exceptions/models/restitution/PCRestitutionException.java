package ch.globaz.pegasus.business.exceptions.models.restitution;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class PCRestitutionException extends PegasusException {

    public PCRestitutionException(String m) {
        super(m);
    }

    public PCRestitutionException(String m, Throwable t) {
        super(m, t);
    }
}

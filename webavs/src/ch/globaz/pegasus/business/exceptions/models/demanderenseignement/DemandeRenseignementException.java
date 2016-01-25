package ch.globaz.pegasus.business.exceptions.models.demanderenseignement;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class DemandeRenseignementException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public DemandeRenseignementException(String message) {
        super(message);
    }

    public DemandeRenseignementException(String message, Exception e) {
        super(message, e);
    }

}

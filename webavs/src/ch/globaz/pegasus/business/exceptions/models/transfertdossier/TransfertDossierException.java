package ch.globaz.pegasus.business.exceptions.models.transfertdossier;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class TransfertDossierException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TransfertDossierException(String message) {
        super(message);
    }

    public TransfertDossierException(String message, Exception e) {
        super(message, e);
    }

}

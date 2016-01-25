package ch.globaz.corvus.business.exceptions.models;

import ch.globaz.corvus.business.exceptions.CorvusException;

public class RERenteJoinPeriodeServiceException extends CorvusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RERenteJoinPeriodeServiceException() {
        super();
    }

    public RERenteJoinPeriodeServiceException(String message) {
        super(message);
    }

    public RERenteJoinPeriodeServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

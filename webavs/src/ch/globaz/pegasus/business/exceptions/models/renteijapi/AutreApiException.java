package ch.globaz.pegasus.business.exceptions.models.renteijapi;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class AutreApiException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AutreApiException() {
        super();
    }

    public AutreApiException(String str) {
        super(str);
    }

    public AutreApiException(String str, Throwable throwable) {
        super(str, throwable);
    }
}

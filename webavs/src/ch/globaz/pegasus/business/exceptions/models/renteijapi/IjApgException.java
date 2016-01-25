package ch.globaz.pegasus.business.exceptions.models.renteijapi;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class IjApgException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public IjApgException() {
        super();
    }

    public IjApgException(String str) {
        super(str);
    }

    public IjApgException(String str, Throwable throwable) {
        super(str, throwable);
    }

}

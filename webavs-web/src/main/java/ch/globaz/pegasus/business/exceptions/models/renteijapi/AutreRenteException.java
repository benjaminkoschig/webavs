package ch.globaz.pegasus.business.exceptions.models.renteijapi;

import ch.globaz.pegasus.business.exceptions.PegasusException;

/**
 * @author DMA
 * @date 24 juin 2010
 */
public class AutreRenteException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AutreRenteException() {
        super();
    }

    public AutreRenteException(String str) {
        super(str);
    }

    public AutreRenteException(String str, Throwable throwable) {
        super(str, throwable);
    }

}

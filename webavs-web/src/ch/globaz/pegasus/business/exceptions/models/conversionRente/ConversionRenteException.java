package ch.globaz.pegasus.business.exceptions.models.conversionRente;

import ch.globaz.pegasus.business.exceptions.PegasusException;

public class ConversionRenteException extends PegasusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ConversionRenteException() {
        super();
    }

    public ConversionRenteException(String m) {
        super(m);
    }

    public ConversionRenteException(String m, Throwable t) {
        super(m, t);
    }
}

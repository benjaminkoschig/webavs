package ch.globaz.perseus.business.exceptions.models.rentepont;

import ch.globaz.perseus.business.exceptions.PerseusException;

/**
 * @author jsi
 * 
 */
public class FactureRentePontException extends PerseusException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FactureRentePontException(String string) {
        super(string);
    }

    public FactureRentePontException(String string, Exception e) {
        super(string, e);
    }

}

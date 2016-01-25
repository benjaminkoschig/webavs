package globaz.cygnus.exceptions;

import globaz.jade.exception.JadeApplicationException;

/**
 * 
 * Exception pour les erreurs métiers survenant dans l'application CYGNUS (RFM)
 * 
 * @author PBA
 * @author vch
 */
public class RFBusinessException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RFBusinessException(String label) {
        super(label);
    }

    public RFBusinessException(String label, Exception parentException) {
        super(label, parentException);
    }
}

package globaz.hera.exception;

import globaz.jade.exception.JadeApplicationException;

/**
 * Exception métier, pouvant être levée dans l'application de la situation familiale (HERA)
 * 
 * @author PBA
 */
public class SFBusinessException extends JadeApplicationException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SFBusinessException(String message) {
        super(message);
    }
}

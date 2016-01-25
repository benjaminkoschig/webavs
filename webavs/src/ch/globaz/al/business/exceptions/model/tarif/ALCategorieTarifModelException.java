package ch.globaz.al.business.exceptions.model.tarif;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des catégories de tarif
 * 
 * @author jts
 */
public class ALCategorieTarifModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALCategorieTarifModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALCategorieTarifModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALCategorieTarifModelException(String m, Throwable t) {
        super(m, t);
    }
}
package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des dossiers
 * 
 * @author jts
 */
public class ALDossierModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDossierModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDossierModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDossierModelException(String m, Throwable t) {
        super(m, t);
    }
}
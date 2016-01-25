package ch.globaz.al.business.exceptions.business;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les services business liés aux dossier
 * 
 * @author jts
 */
public class ALDossierBusinessException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDossierBusinessException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDossierBusinessException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDossierBusinessException(String m, Throwable t) {
        super(m, t);
    }
}

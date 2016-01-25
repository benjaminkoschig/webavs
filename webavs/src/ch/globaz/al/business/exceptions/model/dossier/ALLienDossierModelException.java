package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier des liens dossier
 * 
 * @author gmo
 */
public class ALLienDossierModelException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALLienDossierModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALLienDossierModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALLienDossierModelException(String m, Throwable t) {
        super(m, t);
    }
}

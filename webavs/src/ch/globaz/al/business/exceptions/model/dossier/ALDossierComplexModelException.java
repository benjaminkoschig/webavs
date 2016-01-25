package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les modèles complexes de dossier
 * 
 * @author jts
 * @see ch.globaz.al.business.models.dossier.DossierComplexModel
 */
public class ALDossierComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDossierComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDossierComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDossierComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}

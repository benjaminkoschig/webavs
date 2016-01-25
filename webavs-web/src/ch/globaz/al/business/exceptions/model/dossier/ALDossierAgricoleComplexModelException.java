package ch.globaz.al.business.exceptions.model.dossier;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> métier pour les modèles complexes de dossier agricoles
 * 
 * @author jts
 * @see ch.globaz.al.business.models.dossier.DossierAgricoleComplexModel
 */
public class ALDossierAgricoleComplexModelException extends ALException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALDossierAgricoleComplexModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALDossierAgricoleComplexModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALDossierAgricoleComplexModelException(String m, Throwable t) {
        super(m, t);
    }
}

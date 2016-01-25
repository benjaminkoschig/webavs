package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les récapitulatifs d'entreprise destinés à l'affichage des résultats recherche
 * récap
 * 
 * @author GMO
 * 
 */
public class ALRecapitulatifEntrepriseListModelException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRecapitulatifEntrepriseListModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRecapitulatifEntrepriseListModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRecapitulatifEntrepriseListModelException(String m, Throwable t) {
        super(m, t);
    }

}

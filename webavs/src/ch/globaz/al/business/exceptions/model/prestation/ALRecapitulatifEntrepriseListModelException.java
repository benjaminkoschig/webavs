package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les r�capitulatifs d'entreprise destin�s � l'affichage des r�sultats recherche
 * r�cap
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

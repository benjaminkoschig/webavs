package ch.globaz.al.business.exceptions.model.prestation;

import ch.globaz.al.business.exceptions.ALException;

/**
 * Classe <code>Exception</code> pour les récapitulatifs d'entreprise destinés à l'impression
 * 
 * @author PTA
 * 
 */
public class ALRecapitulatifEntrepriseImpressionModelException extends ALException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @see ALException#ALException()
     */
    public ALRecapitulatifEntrepriseImpressionModelException() {
        super();
    }

    /**
     * @see ALException#ALException(String)
     */
    public ALRecapitulatifEntrepriseImpressionModelException(String m) {
        super(m);
    }

    /**
     * @see ALException#ALException(String, Throwable)
     */
    public ALRecapitulatifEntrepriseImpressionModelException(String m, Throwable t) {
        super(m, t);
    }

}

/*
 * Créé le 3 oct. 07
 */
package globaz.ij.vb.prononces;

import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPrononceAllocAssistance;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class IJPrononceAllocAssistanceViewBean extends IJAbstractPrononceProxyViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private IJPrononceAllocAssistance prononce = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceAllocAssistance.
     */
    public IJPrononceAllocAssistanceViewBean() {
        super(new IJPrononceAllocAssistance());
        prononce = (IJPrononceAllocAssistance) getPrononce();

        prononce.setCsEtat(IIJPrononce.CS_ATTENTE);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter de l'attribut montantTotal
     * 
     * @return
     */
    public String getMontantTotal() {
        return prononce.getMontantTotal();
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return prononce.hasSpy();
    }

    /**
     * setter de l'attribut montantTotal
     * 
     * @param montant
     */
    public void setMontantTotal(String montant) {
        prononce.setMontantTotal(montant);
    }
}

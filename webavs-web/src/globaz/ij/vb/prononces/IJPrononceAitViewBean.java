/*
 * Créé le 3 oct. 07
 */
package globaz.ij.vb.prononces;

import globaz.ij.api.prononces.IIJPrononce;
import globaz.ij.db.prononces.IJPrononceAit;

/**
 * <H1>Description</H1>
 * 
 * @author bsc
 */
public class IJPrononceAitViewBean extends IJAbstractPrononceProxyViewBean {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private IJPrononceAit prononce = null;

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJPrononceAit.
     */
    public IJPrononceAitViewBean() {
        super(new IJPrononceAit());
        prononce = (IJPrononceAit) getPrononce();

        prononce.setCsEtat(IIJPrononce.CS_ATTENTE);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * getter de l'attribut montant
     * 
     * @return
     */
    public String getMontant() {
        return prononce.getMontant();
    }

    /**
     * @return DOCUMENT ME!
     */
    @Override
    public boolean hasSpy() {
        return prononce.hasSpy();
    }

    /**
     * setter de l'attribut montant
     * 
     * @param montant
     */
    public void setMontant(String montant) {
        prononce.setMontant(montant);
    }
}

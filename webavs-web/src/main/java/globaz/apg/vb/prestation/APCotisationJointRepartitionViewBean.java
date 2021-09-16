/*
 * Créé le 18 juil. 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.vb.prestation;

import globaz.apg.api.droits.IAPDroitLAPG;
import globaz.apg.db.prestation.APCotisationJointRepartition;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.util.JANumberFormatter;

/**
 * <H1>Description</H1>
 * 
 * @author vre
 */
public class APCotisationJointRepartitionViewBean extends APCotisationJointRepartition implements FWViewBeanInterface {

    private static final long serialVersionUID = 4913877711690970455L;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.apg.db.prestation.APCotisation#getMontant()
     */
    @Override
    public String getMontant() {
        return JANumberFormatter.fmt(super.getMontant(), true, true, true, 2);
    }

    /**
     * @see globaz.apg.db.prestation.APCotisationJointRepartition#getMontantBrut()
     */
    @Override
    public String getMontantBrut() {
        return JANumberFormatter.fmt(super.getMontantBrut(), true, true, true, 2);
    }

    /**
     * @see globaz.apg.db.prestation.APCotisation#setMontant(java.lang.String)
     */
    @Override
    public void setMontant(String montant) {
        super.setMontant(JANumberFormatter.deQuote(montant));
    }

    /**
     * @see globaz.apg.db.prestation.APCotisationJointRepartition#setMontantBrut(java.lang.String)
     */
    @Override
    public void setMontantBrut(String string) {
        super.setMontantBrut(JANumberFormatter.deQuote(string));
    }

    /**
     * teste si le droit est définitif
     * 
     * @return true si définitive, false sinon
     */
    public boolean isDefinitif() {
        return getEtatDroit().equals(IAPDroitLAPG.CS_ETAT_DROIT_DEFINITIF);
    }
}

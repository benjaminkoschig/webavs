/*
 * Créé le 24 juil. 07
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.db.rentesaccordees.RERenteAccordee;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.jade.client.util.JadeStringUtil;
import globaz.prestation.interfaces.tiers.PRTiersHelper;
import globaz.prestation.interfaces.tiers.PRTiersWrapper;

/**
 * @author bsc
 * 
 */
public class RERenteAccordeeViewBean extends RERenteAccordee implements FWViewBeanInterface {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String montantRetroactif = "";
    private PRTiersWrapper tiersBeneficiaire = null;

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * Donne la chaine "AI" ou "AVS" en fonction de code de la prestion
     * 
     * @return
     */
    public String getAvsOuAi() {
        if (getCodePrestation().equals("50") || getCodePrestation().equals("53") || getCodePrestation().equals("54")
                || getCodePrestation().equals("55") || getCodePrestation().equals("70")
                || getCodePrestation().equals("72") || getCodePrestation().equals("73")
                || getCodePrestation().equals("74") || getCodePrestation().equals("75")
                || getCodePrestation().equals("81") || getCodePrestation().equals("82")
                || getCodePrestation().equals("83") || getCodePrestation().equals("84")
                || getCodePrestation().equals("88") || getCodePrestation().equals("91")
                || getCodePrestation().equals("92") || getCodePrestation().equals("93")) {

            return "AI";
        } else {
            return "AVS";
        }
    }

    /**
     * @return
     */
    public String getMontantRetroactif() {
        return montantRetroactif;
    }

    /**
     * Donne les infos sur le tiers beneficiaire
     * 
     * @return
     */
    public PRTiersWrapper getTiersBeneficiaire() {
        loadTiersBeneficiaire();

        return tiersBeneficiaire;
    }

    /**
     * retrouve les propriétés du Beneficiaire
     */
    private void loadTiersBeneficiaire() {
        if (tiersBeneficiaire == null) {
            if (!JadeStringUtil.isIntegerEmpty(getIdTiersBeneficiaire())) {
                try {
                    tiersBeneficiaire = PRTiersHelper.getTiersParId(getSession(), getIdTiersBeneficiaire());
                } catch (Exception e) {
                    // on net fait rien
                }
            }
        }
    }

    /**
     * @param string
     */
    public void setMontantRetroactif(String string) {
        montantRetroactif = string;
    }

}

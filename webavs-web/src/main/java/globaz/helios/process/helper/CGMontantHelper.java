package globaz.helios.process.helper;

import globaz.framework.util.FWCurrency;

/**
 * @author user
 * 
 *         To change this generated comment edit the template variable "typecomment": Window>Preferences>Java>Templates.
 *         To enable and disable the creation of type comments go to Window>Preferences>Java>Code Generation.
 */
public class CGMontantHelper {

    public FWCurrency actif = null;
    public String idExterne = null;
    public FWCurrency passif = null;
    public FWCurrency soldeCumule = null;

    /**
     * Constructor for CGMontantHelper.
     */
    public CGMontantHelper() {
    }

    public CGMontantHelper add(CGMontantHelper montant) {
        actif.add(montant.actif);
        passif.add(montant.passif);
        soldeCumule.add(montant.soldeCumule);
        return this;
    }

    public CGMontantHelper sub(CGMontantHelper montant) {
        actif.sub(montant.actif);
        passif.sub(montant.passif);
        soldeCumule.sub(montant.soldeCumule);
        return this;
    }
}

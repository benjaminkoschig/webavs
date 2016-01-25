/*
 * Créé le 26 juil. 07
 */

package globaz.corvus.vb.lots;

import globaz.corvus.api.lots.IRELot;
import globaz.corvus.db.lots.RELot;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author BSC
 * 
 */

public class RELotViewBean extends RELot implements FWViewBeanInterface {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    // ~ Methods
    // -----------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @return le libelle du csEtatLot
     */
    public String getCsEtatLotLibelle() {
        return getSession().getCodeLibelle(getCsEtatLot());
    }

    /**
     * 
     * @return le libelle du csTypeLot
     */
    public String getCsTypeLotLibelle() {
        return getSession().getCodeLibelle(getCsTypeLot());
    }

    /**
     * 
     * @return true si csEtat lot = valide
     */
    public boolean isInCsEtatLotValide() {
        return IRELot.CS_ETAT_LOT_VALIDE.equals(getCsEtatLot());
    }

}
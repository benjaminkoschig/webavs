/*
 * Créé le 8 août. 07
 */

package globaz.corvus.vb.taux;

import globaz.corvus.db.taux.RETaux;
import globaz.framework.bean.FWViewBeanInterface;

/**
 * @author BSC
 * 
 */
public class RETauxViewBean extends RETaux implements FWViewBeanInterface {

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
     * @return le libelle du csTypeTaux
     */
    public String getCsTypeTauxLibelle() {
        return getSession().getCodeLibelle(getCsTypeTaux());
    }

}
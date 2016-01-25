/*
 * Créé le 30 juil. 07
 */
package globaz.corvus.vb.prestations;

import globaz.corvus.db.prestations.REPrestationsJointDemandeRenteManager;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 * 
 */
public class REPrestationsJointDemandeRenteListViewBean extends REPrestationsJointDemandeRenteManager {

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    @Override
    protected BEntity _newEntity() throws Exception {
        return new REPrestationsJointDemandeRenteViewBean();
    }

}

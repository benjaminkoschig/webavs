/*
 * Créé le 16 fevr. 07
 */
package globaz.corvus.vb.rentesaccordees;

import globaz.corvus.db.rentesaccordees.REPrestationDue;
import globaz.corvus.db.rentesaccordees.REPrestationsDuesJointDemandeRenteManager;
import globaz.globall.db.BEntity;

/**
 * @author bsc
 * 
 */

public class REPrestationsDuesJointDemandeRenteListViewBean extends REPrestationsDuesJointDemandeRenteManager {

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
        return new REPrestationsDuesJointDemandeRenteViewBean();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REPrestationDue.FIELDNAME_CS_TYPE + ", " + "CASE " + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT
                + " WHEN 0 THEN 999999" + " ELSE " + REPrestationDue.FIELDNAME_DATE_FIN_PAIEMENT + " END" + " DESC";
    }
}
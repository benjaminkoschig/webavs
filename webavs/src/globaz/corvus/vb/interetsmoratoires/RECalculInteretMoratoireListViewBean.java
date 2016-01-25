/*
 * Créé le 2 août 07
 */
package globaz.corvus.vb.interetsmoratoires;

import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoire;
import globaz.corvus.db.interetsmoratoires.RECalculInteretMoratoireManager;
import globaz.corvus.db.rentesaccordees.REInformationsComptabilite;
import globaz.globall.db.BEntity;

/**
 * @author BSC
 * 
 */
public class RECalculInteretMoratoireListViewBean extends RECalculInteretMoratoireManager {

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
        return new RECalculInteretMoratoireViewBean();
    }

    /**
     * (non-Javadoc).
     * 
     * @return la valeur courante de l'attribut order by defaut
     * 
     * @see globaz.prestation.db.PRAbstractManager#getOrderByDefaut()
     */
    @Override
    public String getOrderByDefaut() {
        return REInformationsComptabilite.FIELDNAME_ID_TIERS_ADRESSE_PMT + ", "
                + RECalculInteretMoratoire.FIELDNAME_ID_CALCUL_INTERET_MORATOIRE;
    }

}

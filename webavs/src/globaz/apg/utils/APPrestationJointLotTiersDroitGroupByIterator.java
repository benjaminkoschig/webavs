/*
 * Créé le 18 mai 07
 */
package globaz.apg.utils;

import globaz.apg.db.prestation.APPrestationJointLotTiersDroit;
import globaz.prestation.tools.nnss.PRAbstractGroupByIterator;
import java.util.Iterator;

/**
 * @author HPE
 * 
 */
public class APPrestationJointLotTiersDroitGroupByIterator extends PRAbstractGroupByIterator {

    /**
     * @param iterator
     * @throws NullPointerException
     */
    public APPrestationJointLotTiersDroitGroupByIterator(Iterator iterator) throws NullPointerException {
        super(iterator);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    /**
     * Vrai si les deux objets sont égaux
     * 
     * @return true si les deux objets sont égaux
     */
    @Override
    public boolean areEntitiesEqual(Object e1, Object e2) {

        if (e1 != null && e2 != null) {
            return ((APPrestationJointLotTiersDroit) e1).getIdPrestation().equals(
                    ((APPrestationJointLotTiersDroit) e2).getIdPrestation());
        } else {
            return false;
        }
    }
}
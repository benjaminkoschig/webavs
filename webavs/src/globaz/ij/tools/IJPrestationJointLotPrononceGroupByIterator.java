/*
 * Créé le 16 mai 07
 */

package globaz.ij.tools;

import globaz.ij.vb.prestations.IJPrestationJointLotPrononceViewBean;
import globaz.prestation.tools.nnss.PRAbstractGroupByIterator;
import java.util.Iterator;

/**
 * @author HPE
 * 
 */

public class IJPrestationJointLotPrononceGroupByIterator extends PRAbstractGroupByIterator {

    /**
     * @param iterator
     * @throws NullPointerException
     */
    public IJPrestationJointLotPrononceGroupByIterator(Iterator iterator) throws NullPointerException {
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
            return ((IJPrestationJointLotPrononceViewBean) e1).getIdPrestation().equals(
                    ((IJPrestationJointLotPrononceViewBean) e2).getIdPrestation());
        } else {
            return false;
        }
    }
}
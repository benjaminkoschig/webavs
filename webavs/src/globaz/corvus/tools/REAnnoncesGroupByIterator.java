package globaz.corvus.tools;

import globaz.corvus.vb.annonces.REAnnoncesAbstractLevel1AViewBean;
import globaz.prestation.tools.nnss.PRAbstractGroupByIterator;
import java.util.Iterator;

public class REAnnoncesGroupByIterator extends PRAbstractGroupByIterator {

    /**
     * @param iterator
     * @throws NullPointerException
     */
    public REAnnoncesGroupByIterator(Iterator iterator) throws NullPointerException {
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
            return ((REAnnoncesAbstractLevel1AViewBean) e1).getIdAnnonce().equals(
                    ((REAnnoncesAbstractLevel1AViewBean) e2).getIdAnnonce());
        } else {
            return false;
        }
    }

}

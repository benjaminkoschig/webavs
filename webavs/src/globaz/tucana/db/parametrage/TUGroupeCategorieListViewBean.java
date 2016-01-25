package globaz.tucana.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.tucana.db.parametrage.access.TUGroupeCategorieManager;

/**
 * Représentation visuelle de la liste TUGroupeCategorieManager.java
 * 
 * @author fgo date de création : 22 juin 06
 * @version : version 1.0
 * 
 */
public class TUGroupeCategorieListViewBean extends TUGroupeCategorieManager implements FWListViewBeanInterface {
    /** Table : TUBPGRC */

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new TUGroupeCategorieViewBean();
    }

}

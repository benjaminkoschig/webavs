package globaz.tucana.db.parametrage;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.tucana.db.parametrage.access.TUGroupeCategorieManager;

/**
 * Repr�sentation visuelle de la liste TUGroupeCategorieManager.java
 * 
 * @author fgo date de cr�ation : 22 juin 06
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
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new TUGroupeCategorieViewBean();
    }

}

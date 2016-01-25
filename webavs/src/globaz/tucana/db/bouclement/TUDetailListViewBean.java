package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.tucana.db.bouclement.access.TUDetailManager;

/**
 * Classe TUDetailListViewBean liste les d�tails de bouclement
 * 
 * @author fgo date de cr�ation : 11 mai 06
 * @version : version 1.0
 * 
 */

public class TUDetailListViewBean extends TUDetailManager implements FWListViewBeanInterface {
    /** Table : TUBPDET */

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
        return new TUDetailViewBean();
    }
}

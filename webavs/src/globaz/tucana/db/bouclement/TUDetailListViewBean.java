package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.tucana.db.bouclement.access.TUDetailManager;

/**
 * Classe TUDetailListViewBean liste les détails de bouclement
 * 
 * @author fgo date de création : 11 mai 06
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
     * Instancie un objet étendant BEntity
     * 
     * @return BEntity un objet repésentant le résultat
     * @throws Exception
     *             la création a échouée
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new TUDetailViewBean();
    }
}

package globaz.corvus.vb.recap;

import globaz.corvus.db.recap.access.RERecapElementManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:45:28 CET 2007
 */
public class RERecapElementListViewBean extends RERecapElementManager implements FWListViewBeanInterface {
    /** Table : REELMREC */

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
        return new RERecapElementViewBean();
    }

}

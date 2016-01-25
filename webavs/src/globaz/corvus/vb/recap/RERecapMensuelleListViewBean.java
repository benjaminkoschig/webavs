package globaz.corvus.vb.recap;

import globaz.corvus.db.recap.access.RERecapMensuelleManager;
import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author ${user}
 * 
 * @version 1.0 Created on Fri Nov 30 11:35:47 CET 2007
 */
public class RERecapMensuelleListViewBean extends RERecapMensuelleManager implements FWListViewBeanInterface {
    /** Table : RERECMEN */

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
        return new RERecapMensuelleViewBean();
    }

}

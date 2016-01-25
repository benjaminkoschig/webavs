package globaz.tucana.db.bouclement;

import globaz.framework.bean.FWListViewBeanInterface;
import globaz.globall.db.BEntity;
import globaz.tucana.db.bouclement.access.TUNoPassageManager;

/**
 * @author fgo
 * 
 * @version 1.0 Created on Fri May 05 15:07:59 CEST 2006
 */
public class TUNoPassageListViewBean extends TUNoPassageManager implements FWListViewBeanInterface {
    /** Table : TUBPNP */

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
        return new TUNoPassageViewBean();
    }

}

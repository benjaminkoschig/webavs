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
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new RERecapMensuelleViewBean();
    }

}

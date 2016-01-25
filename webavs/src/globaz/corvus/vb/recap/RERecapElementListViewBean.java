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
     * Instancie un objet �tendant BEntity
     * 
     * @return BEntity un objet rep�sentant le r�sultat
     * @throws Exception
     *             la cr�ation a �chou�e
     */
    @Override
    public BEntity _newEntity() throws Exception {
        return new RERecapElementViewBean();
    }

}

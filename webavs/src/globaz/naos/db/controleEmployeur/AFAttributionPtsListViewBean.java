package globaz.naos.db.controleEmployeur;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * Ins�rez la description du type ici. Date de cr�ation : (10.05.2002 09:35:05)
 * 
 * @author: Administrator
 */
public class AFAttributionPtsListViewBean extends AFAttributionPtsManager implements FWViewBeanInterface {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public AFAttributionPtsListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new AFAttributionPtsViewBean();
    }
}

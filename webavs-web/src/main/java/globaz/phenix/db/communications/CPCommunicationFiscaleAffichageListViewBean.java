/*
 * Created on 24 nov. 05
 */
package globaz.phenix.db.communications;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BEntity;

/**
 * @author mar
 */
public class CPCommunicationFiscaleAffichageListViewBean extends CPCommunicationFiscaleAffichageManager implements
        FWViewBeanInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public CPCommunicationFiscaleAffichageListViewBean() {
        super();
    }

    @Override
    protected BEntity _newEntity() throws Exception {
        return new CPCommunicationFiscaleAffichageViewBean();
    }

}

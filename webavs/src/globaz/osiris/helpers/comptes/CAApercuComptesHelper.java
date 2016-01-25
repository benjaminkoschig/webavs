/*
 * Créé le 7 févr. 06
 */
package globaz.osiris.helpers.comptes;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.db.BIPersistentObject;
import globaz.osiris.db.comptes.CACompteAnnexeViewBean;

/**
 * @author sch date : 7 févr. 06
 */
public class CAApercuComptesHelper extends FWHelper {
    /**
	 *
	 */
    public CAApercuComptesHelper() {
        super();
    }

    @Override
    protected void _retrieve(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        ((BIPersistentObject) viewBean).retrieve();
        ((CACompteAnnexeViewBean) viewBean)._initialise();
    }
}

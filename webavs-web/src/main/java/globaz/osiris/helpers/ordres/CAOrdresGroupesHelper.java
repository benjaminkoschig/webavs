package globaz.osiris.helpers.ordres;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.controller.FWHelper;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.jade.log.JadeLogger;
import globaz.osiris.api.ordre.APIOrdreGroupe;
import globaz.osiris.db.ordres.CAOrdreGroupeViewBean;

public class CAOrdresGroupesHelper extends FWHelper {
    public CAOrdresGroupesHelper() {
        super();
    }

    @Override
    protected FWViewBeanInterface execute(FWViewBeanInterface viewBean, FWAction action, BISession session) {

        if ("valider".equals(action.getActionPart())) {
            CAOrdreGroupeViewBean vb = (CAOrdreGroupeViewBean) viewBean;
            vb.setSession((BSession) session);

            try {
                vb.retrieve();

                if (!vb.isNew()) {
                    vb.setIsoCsTransmissionStatutExec(APIOrdreGroupe.ISO_TRANSAC_STATUS_VALIDE);
                    vb.setSession((BSession) session);
                    vb.update();
                }
            } catch (Exception e) {
                JadeLogger.error(this, e);
            }
        }

        return super.execute(viewBean, action, session);
    }
}
